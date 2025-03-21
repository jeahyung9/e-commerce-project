package com.fullstack.springboot.service.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.springboot.dto.order.OrderDTO;
import com.fullstack.springboot.dto.order.OrderDetailDTO;
import com.fullstack.springboot.dto.order.OrderStatus;
import com.fullstack.springboot.dto.order.RefundDTO;
import com.fullstack.springboot.dto.product.ProductImageDTO;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.order.Order;
import com.fullstack.springboot.entity.order.OrderDetail;
import com.fullstack.springboot.entity.order.Payment;
import com.fullstack.springboot.entity.order.Refund;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.external.payment.exception.ErrorCode;
import com.fullstack.springboot.external.payment.exception.PaymentException;
import com.fullstack.springboot.repository.order.OrderRepository;
import com.fullstack.springboot.repository.order.RefundRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;
    
    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        log.info("========== 주문 및 주문상세 생성 서비스 시작 ==========");
        log.debug("주문 정보: {}", orderDTO);

        try {
            validateOrderDTO(orderDTO);

            Order order = Order.builder()
                    .o_totalPrice(orderDTO.getO_totalPrice())
                    .o_address(orderDTO.getO_address())    
                    .o_reciver(orderDTO.getO_reciver())
                    .o_reciverPhoNum(orderDTO.getO_reciverPhoNum())
                    .status(OrderStatus.ORDER_COMPLETE)
                    .orderDate(LocalDateTime.now())
                    .member(Member.builder().mno(orderDTO.getMno()).build())
                    .build();

            List<OrderDetail> orderDetails = orderDTO.getOrderDetails().stream()
            	    .map(detailDTO -> {
            	        log.debug("Processing OrderDetailDTO: {}", detailDTO);
            	        if (detailDTO.getOdno() == null) {
            	            log.error("OptionDetail odno is null for OrderDetailDTO: {}", detailDTO);
            	        }
            	        return OrderDetail.builder()
            	                .or_price(detailDTO.getOr_price())
            	                .or_count(detailDTO.getOr_count())
            	                .order(order)
            	                .optionDetail(OptionDetail.builder().odno(detailDTO.getOdno()).build())
            	                .build();
            	    })
            	    .collect(Collectors.toList());

            order.setOrderDetails(orderDetails);

            log.debug("Order before save: {}", order);
            Order savedOrder = orderRepository.save(order);
            log.info("주문 저장 완료 - 주문번호: {}", savedOrder.getOno());

            return convertToDTO(savedOrder);

        } catch (Exception e) {
            log.error("주문 생성 중 오류 발생: {}", e.getMessage());
            throw new PaymentException(ErrorCode.ORDER_CREATION_FAILED);
        }
    }
    
    //다 주문 조회
    @Override
    public List<OrderDTO> getAllOrders() {
        log.info("========== 모든 주문 목록 조회 시작 ==========");
        
        List<Order> orderList = orderRepository.findAll();
        List<OrderDTO> orderDTOList = orderList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        log.info("모든 주문 조회 완료 - 주문 수: {}", orderDTOList.size());
        return orderDTOList;
    }
    
    //ono로 주문 조회
    @Override
    public OrderDTO getOrder(Long ono) {
        log.info("========== 주문 조회 서비스 시작 ==========");
        log.debug("조회 주문번호: {}", ono);

        Order order = orderRepository.findById(ono)
                .orElseThrow(() -> {
                    log.error("주문을 찾을 수 없음 - 주문번호: {}", ono);
                    return new EntityNotFoundException("주문을 찾을 수 없습니다: " + ono);
                });

        log.info("주문 조회 완료 - 주문번호: {}", ono);
        return convertToDTO(order);
    }
    
    //미구현
    @Override
    @Transactional
    public OrderDTO cancelOrder(Long ono) {
        log.info("========== 주문 취소 서비스 시작 ==========");
        log.debug("취소 주문번호: {}", ono);

        Order order = orderRepository.findById(ono)
                .orElseThrow(() -> {
                    log.error("취소할 주문을 찾을 수 없음 - 주문번호: {}", ono);
                    return new EntityNotFoundException("주문을 찾을 수 없습니다: " + ono);
                });

        //order.setOrderState(false);
        order.setStatus(OrderStatus.CANCEL); // 상태를 CANCEL로 변경
        Order canceledOrder = orderRepository.save(order);
        
        log.info("주문 취소 완료 - 주문번호: {}", ono);
        return convertToDTO(canceledOrder);
    }
    
    //mno로 주문 목록 조회
    @Override
    public List<OrderDTO> getUserOrders(Long mno) {
        log.info("========== 회원 주문 목록 조회 시작22222 ==========");
        log.debug("회원번호: {}", mno);

        List<Order> orderList = orderRepository.findByMember_Mno(mno);
        List<OrderDTO> orderDTOList = orderList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        for(OrderDTO dto : orderDTOList) {
        	log.info("회원 주문 조회 완료 : ", dto);        	
        }
        log.info("회원 주문 조회 완료 - 주문 수: {}", orderDTOList.size());
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        log.info("========== 주문 상태별 목록 조회 시작 ==========");
        log.debug("조회할 주문 상태: {}", status);
        
        List<Order> orderList;
        if (status == null) {
            orderList = orderRepository.findAll();
        } else {
            orderList = orderRepository.findByStatus(status);
        }
        
        List<OrderDTO> orderDTOList = orderList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        log.info("주문 상태별 조회 완료 - 상태: {}, 주문 수: {}", status, orderDTOList.size());
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> getUserOrdersByStatus(Long mno, OrderStatus status) {
        log.info("========== 회원별 주문 상태 목록 조회 시작 ==========");
        log.debug("회원번호: {}, 주문 상태: {}", mno, status);
        
        List<Order> orderList;
        if (status == null) {
            orderList = orderRepository.findByMember_Mno(mno);
        } else {
            orderList = orderRepository.findByMember_MnoAndStatus(mno, status);
        }
        
        List<OrderDTO> orderDTOList = orderList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        log.info("회원별 주문 상태 조회 완료 - 회원번호: {}, 상태: {}, 주문 수: {}", 
                 mno, status, orderDTOList.size());
        return orderDTOList;
    }
    
    // 필수 필드 검증 메서드
    private void validateOrderDTO(OrderDTO orderDTO) {
        if (orderDTO.getO_totalPrice() == null) {
            throw new PaymentException(ErrorCode.ORDER_CREATION_FAILED, "총 주문 금액은 필수입니다.");
        }
        if (orderDTO.getO_address() == null || orderDTO.getO_address().trim().isEmpty()) {
            throw new PaymentException(ErrorCode.ORDER_CREATION_FAILED, "배송 주소는 필수입니다.");
        }
        if (orderDTO.getO_reciver() == null || orderDTO.getO_reciver().trim().isEmpty()) {
            throw new PaymentException(ErrorCode.ORDER_CREATION_FAILED, "수령인은 필수입니다.");
        }
        if (orderDTO.getO_reciverPhoNum() == null || orderDTO.getO_reciverPhoNum().trim().isEmpty()) {
            throw new PaymentException(ErrorCode.ORDER_CREATION_FAILED, "수령인 연락처는 필수입니다.");
        }
        if (orderDTO.getMno() == null) {
            throw new PaymentException(ErrorCode.ORDER_CREATION_FAILED, "회원 정보는 필수입니다.");
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
    private OrderDTO convertToDTO(Order order) {
        Payment payment = order.getPayment();
        Long pmno = (payment != null) ? payment.getPmno() : null;

        // RefundDTO 초기화
        RefundDTO refundDTO = null;
        if (pmno != null) {
            Refund refund = refundRepository.findByPayment_Pmno(pmno).orElse(null);
            if (refund != null) {
                refundDTO = RefundDTO.builder()
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
            } else {
                log.warn("환불 정보를 찾을 수 없음 - pmno: {}", pmno);
            }
        } else {
            log.warn("결제 정보가 없음 - 주문번호: {}", order.getOno());
        }

        List<OrderDetailDTO> orderDetails = order.getOrderDetails().stream()
            .map(detail -> {
                OptionDetail optionDetail = detail.getOptionDetail();
                Product product = null;
                List<ProductImageDTO> productImages = new ArrayList<>();  // 빈 리스트로 초기화
                
                if (optionDetail == null) {
                    log.warn("OptionDetail is null for OrderDetail with orno: {}", detail.getOrno());
                } else {
                    product = optionDetail.getProduct();
                    if (product == null) {
                        log.warn("Product is null for OptionDetail with odno: {}", optionDetail.getOdno());
                    } else {
                        try {
                            // Hibernate.initialize(product.getProductImage());  // 필요한 경우 활성화
                            if (product.getProductImage() != null) {
                                productImages = product.getProductImage().stream()
                                    .filter(image -> image != null && image.getPi_path() != null)
                                    .map(image -> ProductImageDTO.builder()
                                        .pino(image.getPino())
                                        .pi_name(image.getPi_name())
                                        .pi_path(image.getPi_path())
                                        .pi_ord(image.getPi_ord())
                                        //.pno(product.getPno())
                                        .pno(image.getProduct().getPno())  // product.getPno() -> image.getProduct().getPno()로 수정
                                        .build())
                                    .sorted((img1, img2) -> Integer.compare(img1.getPi_ord(), img2.getPi_ord()))
                                    .collect(Collectors.toList());
                            }
                            log.debug("Product {} has {} images", product.getPno(), productImages.size());
                        } catch (Exception e) {
                            log.error("Error processing product images for product {}: {}", product.getPno(), e.getMessage());
                            // 에러가 발생해도 빈 리스트 유지
                        }
                    }
                }

                OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                        .orno(detail.getOrno())
                        .or_price(detail.getOr_price())
                        .or_count(detail.getOr_count())
                        .ono(detail.getOrder().getOno())
                        .odno(optionDetail != null ? optionDetail.getOdno() : null)
                        .od_name(optionDetail != null ? optionDetail.getOd_name() : null)
                        .rno(detail.getRno())
                        .pno(product != null ? product.getPno() : null)
                        .p_name(product != null ? product.getP_name() : null)
                        .p_content(product != null ? product.getP_content() : null)
                        .p_price(product != null ? product.getP_price() : null)
                        .p_salePer(product != null ? product.getP_salePer() : 0)
                        .p_stock(product != null ? product.getP_stock() : 0)
                        .p_salesVol(product != null ? product.getP_salesVol() : 0)
                        .productImage(productImages)  // 수정된 부분
                        .build();

                return orderDetailDTO;
            })
            .collect(Collectors.toList());

        return OrderDTO.builder()
                .ono(order.getOno())
                .o_totalPrice(order.getO_totalPrice())
                .o_address(order.getO_address())
                .o_reciver(order.getO_reciver())
                .o_reciverPhoNum(order.getO_reciverPhoNum())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .mno(order.getMember().getMno())
                .orderDetails(orderDetails)
                .refundDTO(refundDTO)
                .build();
    }
} 