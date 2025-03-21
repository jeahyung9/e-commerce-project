// external/payment/service/IamportServiceImpl.java
package com.fullstack.springboot.external.payment.service;

import com.fullstack.springboot.dto.order.PaymentDTO;
import com.fullstack.springboot.external.payment.IamportClient;
import com.fullstack.springboot.external.payment.exception.PaymentException;
import com.fullstack.springboot.external.payment.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamportServiceImpl implements IamportService {

    private final IamportClient iamportClient;

    @Override
    public long getPaymentAmount(String imp_uid) {
        log.info("========== 아임포트 결제 금액 조회 시작 ==========");
        log.debug("imp_uid: {}", imp_uid);

        try {
            long amount = iamportClient.getPaymentAmount(imp_uid);
            log.info("결제 금액 조회 완료 - 금액: {}", amount);
            return amount;
        } catch (Exception e) {
            log.error("결제 금액 조회 실패: {}", e.getMessage(), e);
            throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_INQUIRY_FAILED, e);
        }
    }



    @Override
    public String getAccessToken() {
        log.info("========== 아임포트 토큰 발급 시작 ==========");

        try {
            String token = iamportClient.getAccessToken();
            log.info("토큰 발급 완료");
            return token;
        } catch (Exception e) {
            log.error("토큰 발급 실패: {}", e.getMessage(), e);
            throw new PaymentException(ErrorCode.IAMPORT_TOKEN_ISSUANCE_FAILED, e);
        }
    }

    @Override
    public String completePayment(PaymentDTO paymentDTO) {
        log.info("========== 결제 완료 시작 ==========");
        log.debug("결제 정보: {}", paymentDTO);

        try {
            // 결제 완료 로직을 구현하세요.
            // 예시: return iamportClient.completePayment(paymentDTO);
            String impUid = "generated-imp-uid"; // 실제 구현에 따라 반환값을 설정하세요.
            log.info("결제 완료 - impUid: {}", impUid);
            return impUid;
        } catch (Exception e) {
            log.error("결제 완료 실패: {}", e.getMessage(), e);
            throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_COMPLETION_FAILED, e);
        }
    }
}