package com.fullstack.springboot.service.order;

import java.util.List;
import com.fullstack.springboot.dto.order.PaymentDTO;
import com.fullstack.springboot.entity.order.Payment;

/**
 * 결제 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 * 결제 생성, 검증, 조회 등의 기능을 정의합니다.
 */
public interface PaymentService {
    
    /**
     * 새로운 결제 정보를 생성합니다.
     * 주문 완료 시 호출되어 결제 정보를 저장합니다.
     * 
     * @param paymentdto 결제 정보 (주문번호, 결제금액 등)
     * @return 생성된 결제 정보
     * @throws EntityNotFoundException 주문을 찾을 수 없는 경우
     */
    PaymentDTO createPayment(PaymentDTO paymentdto);
    
    
    /**
     * 아임포트 결제 검증을 수행합니다.
     * 결제 완료 후 금액과 상태를 검증합니다.
     * 
     * @param imp_uid 아임포트 결제 번호
     * @param pmno 결제 번호
     * @return 검증 결과 (true: 성공, false: 실패)
     * @throws EntityNotFoundException 결제 정보를 찾을 수 없는 경우
     * @throws PaymentException 결제 금액 불일치 등 검증 실패 시
     */
    Boolean verifyPayment(String imp_uid, Long pmno);
    
    /**
     * 결제 번호로 결제 정보를 조회합니다.
     * 
     * @param pmno 결제 번호
     * @return 결제 정보
     * @throws EntityNotFoundException 결제 정보를 찾을 수 없는 경우
     */
    
    PaymentDTO getPayment(Long pmno);
    
    /**
     * 특정 회원의 모든 결제 내역을 조회합니다.
     * 마이페이지의 결제 내역 조회에서 사용됩니다.
     * 
     * @param mno 회원번호
     * @return 결제 내역 목록
     */
    List<PaymentDTO> getMemberPayments(Long mno);
    
    
    
    
    
    
    /**
     * 주문 번호로 결제 정보를 조회합니다.
     * 
     * @param ono 주문 번호
     * @return 결제 정보
     * @throws EntityNotFoundException 결제 정보를 찾을 수 없는 경우
     */
    Payment getPaymentByOrderNumber(Long ono);
    
    
    
    
    
    
    
    
    
    /**
     * Payment 객체를 PaymentDTO로 변환합니다.
     * 
     * @param payment 결제 객체
     * @return 변환된 PaymentDTO
     */
    PaymentDTO convertToDTO(Payment payment);
    
}