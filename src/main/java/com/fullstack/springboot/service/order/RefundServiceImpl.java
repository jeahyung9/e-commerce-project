package com.fullstack.springboot.service.order;

import com.fullstack.springboot.dto.order.IamportApiResult;
import com.fullstack.springboot.dto.order.OrderStatus;
import com.fullstack.springboot.dto.order.RefundDTO;
import com.fullstack.springboot.entity.order.Order;
import com.fullstack.springboot.entity.order.Payment;
import com.fullstack.springboot.entity.order.Refund;
import com.fullstack.springboot.external.payment.IamportClient;
import com.fullstack.springboot.external.payment.exception.PaymentException;
import com.fullstack.springboot.repository.order.OrderRepository;
import com.fullstack.springboot.repository.order.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final IamportClient iamportClient;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public RefundDTO createRefundForOrder(Long ono, String rf_reason, String imp_uid, String merchant_uid) {
        Payment payment = paymentService.getPaymentByOrderNumber(ono);
        if (payment == null) {
            throw new PaymentException("결제 정보를 찾을 수 없음");
        }

        Optional<Refund> existingRefund = refundRepository.findByPayment_Pmno(payment.getPmno());
        if (existingRefund.isPresent() && !existingRefund.get().isRf_status()) {
            throw new PaymentException("이미 환불 요청이 존재합니다.");
        }

        Refund refund = Refund.builder()
                .rf_payment(payment.getAmount())
                .rf_reason(rf_reason)
                .imp_uid(imp_uid)
                .merchant_uid(merchant_uid)
                .rf_status(false)
                .rf_requestDate(LocalDateTime.now())
                .payment(payment)
                .build();

        // Save the refund and ensure rfno is generated
        Refund savedRefund = refundRepository.save(refund);
        log.info("환불 요청 저장 완료 - 환불번호: {}, 주문번호: {}, 결제금액: {}", savedRefund.getRfno(), ono, savedRefund.getRf_payment());

        Optional<Order> optionalOrder = orderRepository.findById(ono);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.REFUND_PENDING);
            orderRepository.save(order);
            log.info("주문 상태 업데이트 완료 - 주문번호: {}, 상태: {}", ono, order.getStatus());
        } else {
            log.error("주문 정보를 찾을 수 없습니다 - 주문번호: {}", ono);
        }

        return convertToDTO(savedRefund);
    }

    @Override
    @Transactional
    public IamportApiResult approveRefundRequest(Long rfno) {
        log.info("환불 승인 처리 시작 - 환불번호: {}", rfno);
        
        // rfno가 null인지 확인
        if (rfno == null) {
            log.error("환불 번호가 null입니다.");
            throw new PaymentException("환불 번호가 null입니다.");
        }

        Refund refund = refundRepository.findById(rfno)
            .orElseThrow(() -> new PaymentException("환불 정보를 찾을 수 없습니다: " + rfno));

        try {
            // 결제 취소 API 호출
            log.info("아임포트 결제 취소 API 호출 - 환불번호: {}, imp_uid: {}", rfno, refund.getImp_uid());
            IamportApiResult cancelResult = iamportClient.cancelPayment(refund.getImp_uid(), refund.getMerchant_uid(), refund.getRf_reason());

            // 환불 상태 업데이트
            refund.setRf_status(true);
            refund.setRf_completeDate(LocalDateTime.now());
            refundRepository.save(refund);
            log.info("환불 상태 업데이트 완료 - 환불번호: {}, 결제금액: {}, 완료일자: {}", 
                rfno, refund.getRf_payment(), refund.getRf_completeDate());

            // 주문 상태 업데이트
            Payment payment = refund.getPayment();
            if (payment != null && payment.getOrder() != null) {
                Order order = payment.getOrder();
                order.setStatus(OrderStatus.REFUND_COMPLETE);
                orderRepository.save(order);
                log.info("주문 상태 업데이트 완료 - 주문번호: {}, 상태: REFUND_COMPLETE", order.getOno());
            } else {
                log.error("주문 정보를 찾을 수 없습니다 - 환불번호: {}", rfno);
                throw new PaymentException("환불 정보 없음 - 주문: " + (payment != null ? payment.getOrder().getOno() : "알 수 없음"));
            }

            return cancelResult;

        } catch (PaymentException e) {
            log.error("결제 취소 실패 - 환불번호: {}, 오류: {}", rfno, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("환불 처리 중 오류 발생 - 환불번호: {}, 오류: {}", rfno, e.getMessage());
            throw new PaymentException("환불 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private RefundDTO convertToDTO(Refund refund) {
        return RefundDTO.builder()
                .rfno(refund.getRfno())
                .rf_payment(refund.getRf_payment())
                .rf_reason(refund.getRf_reason())
                .rf_status(refund.isRf_status())
                .rf_requestDate(refund.getRf_requestDate())
                .rf_completeDate(refund.getRf_completeDate())
                .pmno(refund.getPayment().getPmno())
                .imp_uid(refund.getImp_uid())
                .merchant_uid(refund.getMerchant_uid())
                .build();
    }
    
    
    
    
    
    
    
    
    
    @Override
    public List<RefundDTO> getMemberRefunds(Long mno) {
        log.info("========== 회원 환불 내역 조회 시작 ==========");
        log.debug("회원번호: {}", mno);

        List<Refund> refunds = refundRepository.findByMemberMno(mno);
        return refunds.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDTO> getMemberRefundsByStatus(Long mno, boolean status) {
        log.info("========== 회원 상태별 환불 내역 조회 시작 ==========");
        log.debug("회원번호: {}, 상태: {}", mno, status);

        List<Refund> refunds = refundRepository.findByMemberMnoAndStatus(mno, status);
        return refunds.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}