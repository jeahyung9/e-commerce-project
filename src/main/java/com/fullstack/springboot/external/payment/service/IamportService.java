// external/payment/service/IamportService.java
package com.fullstack.springboot.external.payment.service;

import com.fullstack.springboot.dto.order.PaymentDTO;
import com.fullstack.springboot.external.payment.exception.PaymentException;

/**
 * 아임포트 결제/환불 API 연동을 위한 서비스 인터페이스
 */
public interface IamportService {
    
    /**
     * 결제 금액을 조회합니다.
     * 결제 검증 시 사용됩니다.
     * 
     * @param imp_uid 아임포트 결제 고유번호
     * @return 결제 금액
     * @throws PaymentException 아임포트 API 호출 실패 시
     */
    long getPaymentAmount(String imp_uid);
    
//    /**
//     * 결제를 취소하고 환불을 진행합니다.
//     * 
//     * @param imp_uid 아임포트 결제 고유번호
//     * @param merchant_uid 상점 거래 고유번호
//     * @param reason 취소/환불 사유
//     * @throws PaymentException 아임포트 API 호출 실패 시
//     */
//    void cancelPayment(String imp_uid, String merchant_uid, String reason);
    
    /**
     * 액세스 토큰을 발급받습니다.
     * 아임포트 API 호출 시 인증에 사용됩니다.
     * 
     * @return 액세스 토큰
     * @throws PaymentException 토큰 발급 실패 시
     */
    String getAccessToken();
    
    /**
     * 결제를 완료하고 impUid를 반환합니다.
     * 
     * @param paymentDTO 결제 정보
     * @return impUid 결제 고유번호
     * @throws PaymentException 결제 완료 실패 시
     */
    String completePayment(PaymentDTO paymentDTO);
}