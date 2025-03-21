package com.fullstack.springboot.service.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.springboot.dto.order.PaymentDTO;
import com.fullstack.springboot.entity.order.Order;
import com.fullstack.springboot.entity.order.Payment;
import com.fullstack.springboot.external.payment.exception.ErrorCode;
import com.fullstack.springboot.external.payment.exception.PaymentException;
import com.fullstack.springboot.external.payment.service.IamportService;
import com.fullstack.springboot.repository.order.OrderRepository;
import com.fullstack.springboot.repository.order.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final IamportService iamportService;

    @Override
    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        log.info("========== 결제 생성 시작 ==========");
        log.debug("결제 정보: {}", paymentDTO);

        // 필수 필드 유효성 검사
        if (paymentDTO.getOno() == null) {
            throw new IllegalArgumentException("주문 번호(ono)가 누락되었습니다.");
        }
        if (paymentDTO.getPm_method() == null) {
            throw new IllegalArgumentException("결제 방법(pm_method)이 누락되었습니다.");
        }
        if (paymentDTO.getAmount() == null) {
            throw new IllegalArgumentException("결제 금액(amount)이 누락되었습니다.");
        }
        if (paymentDTO.getImp_uid() == null) {
            throw new IllegalArgumentException("아임포트 UID(impUid)가 누락되었습니다.");
        }
        if (paymentDTO.getMerchant_uid() == null) {
            throw new IllegalArgumentException("상점 거래 UID(merchantUid)가 누락되었습니다.");
        }

        try {
            Order order = orderRepository.findById(paymentDTO.getOno())
                    .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다: " + paymentDTO.getOno()));

            Payment payment = Payment.builder()
                    .pm_method(paymentDTO.getPm_method())
                    .amount(paymentDTO.getAmount())
                    .imp_uid(paymentDTO.getImp_uid())
                    .merchant_uid(paymentDTO.getMerchant_uid())
                    .order(order)
                    .build();

            Payment savedPayment = paymentRepository.save(payment);
            log.info("결제 저장 완료 - 결제번호: {}", savedPayment.getPmno());

            // pmno 로그 출력
            log.debug("저장된 결제의 pmno: {}", savedPayment.getPmno());

            return convertToDTO(savedPayment);

        } catch (EntityNotFoundException e) {
            log.error("주문 조회 중 오류 발생: {}", e.getMessage());
            throw new PaymentException(ErrorCode.ORDER_NOT_FOUND, e);
        } catch (Exception e) {
            log.error("결제 생성 중 일반 오류 발생: {}", e.getMessage(), e);
            throw new PaymentException(ErrorCode.PAYMENT_CREATION_FAILED, e);
        }
    }

    
    @Override
    @Transactional
    public Boolean verifyPayment(String imp_uid, Long pmno) {
        log.info("========== 결제 검증 시작 ==========");
        log.debug("검증 정보: imp_uid={}, 결제번호={}", imp_uid, pmno);

        try {
            // pmno를 사용하여 결제 정보 조회
            Payment payment = paymentRepository.findByPmno(pmno)
                    .orElseThrow(() -> new EntityNotFoundException("결제 정보를 찾을 수 없습니다: " + pmno));

            // imp_uid를 사용하여 아임포트에서 결제 금액 조회
            long iamportAmount = iamportService.getPaymentAmount(imp_uid);

            // 결제 금액 비교
            if (iamportAmount != payment.getOrder().getO_totalPrice()) {
                log.error("결제 금액 불일치 - 아임포트 금액: {}, 주문 금액: {}", iamportAmount, payment.getOrder().getO_totalPrice());
                throw new PaymentException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
            }

            log.info("결제 검증 완료 - 결제번호: {}", pmno);
            return true;

        } catch (Exception e) {
            log.error("결제 검증 중 오류 발생: {}", e.getMessage());
            throw new PaymentException(ErrorCode.PAYMENT_VERIFICATION_FAILED, e);
        }
    }
    
    

    
    
    
    
    
    @Override
    public PaymentDTO getPayment(Long pmno) {
        log.info("========== 결제 정보 조회 시작 ==========");
        log.debug("결제번호: {}", pmno);

        Payment payment = paymentRepository.findById(pmno)
                .orElseThrow(() -> {
                    log.error("결제 정보를 찾을 수 없음 - 결제번호: {}", pmno);
                    return new EntityNotFoundException("결제 정보를 찾을 수 없습니다: " + pmno);
                });

        log.info("결제 정보 조회 완료 - 결제번호: {}", pmno);
        return convertToDTO(payment);
    }

    
    
    
    
    
    @Override
    public List<PaymentDTO> getMemberPayments(Long mno) {
        log.info("========== 회원 결제 내역 조회 시작 ==========");
        log.debug("회원번호: {}", mno);

        List<Payment> paymentList = paymentRepository.findByOrder_Member_Mno(mno);
        List<PaymentDTO> paymentDTOList = paymentList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        log.info("회원 결제 내역 조회 완료 - 결제 수: {}", paymentDTOList.size());
        return paymentDTOList;
    }
    
    
    

    //환불할때 ono 가져오는 로직
    @Override
    public Payment getPaymentByOrderNumber(Long ono) {
        log.info("========== 주문 번호로 결제 정보 조회 시작 ==========");
        log.debug("주문번호: {}", ono);

        Payment payment = paymentRepository.findByOrder_Ono(ono)
                .orElseThrow(() -> new EntityNotFoundException("결제 정보를 찾을 수 없습니다: " + ono));

        // impUid와 merchantUid를 가져와 로그로 출력
        String imp_uid = payment.getImp_uid();
        String merchant_uid = payment.getMerchant_uid();
        log.debug("조회된 결제 정보 - imp_uid: {}, merchant_uid: {}", imp_uid, merchant_uid);

        return payment;
    }
    
    
    

    @Override
    public PaymentDTO convertToDTO(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment 객체가 null입니다.");
        }
        return PaymentDTO.builder()
                .pmno(payment.getPmno())
                .pm_method(payment.getPm_method())
                .amount(payment.getAmount())
                .imp_uid(payment.getImp_uid())
                .merchant_uid(payment.getMerchant_uid())
                .ono(payment.getOrder().getOno())
                .build();
    }
}