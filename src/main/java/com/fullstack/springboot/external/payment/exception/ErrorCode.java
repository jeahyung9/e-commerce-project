package com.fullstack.springboot.external.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 결제 관련 에러
    PAYMENT_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 1001, "결제 생성 실패"),
    PAYMENT_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, 1002, "결제 검증 실패"),
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, 1003, "결제 금액 불일치"),
    PAYMENT_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, 1004, "이미 취소된 결제"),
    PAYMENT_CANCELLATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 1005, "결제 취소 실패"),
    PAYMENT_ALREADY_REFUNDED(HttpStatus.BAD_REQUEST, 1006, "이미 환불된 결제"),
    PAYMENT_COMPLETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 1007, "결제 완료 실패"),
    PAYMENT_NOT_COMPLETED(HttpStatus.BAD_REQUEST, 1008, "결제가 완료되지 않음"),

    // 아임포트 관련 에러
    IAMPORT_TOKEN_ISSUANCE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 3001, "아임포트 토큰 발급 실패"),
    IAMPORT_PAYMENT_INQUIRY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 3002, "아임포트 결제 조회 실패"),
    IAMPORT_PAYMENT_CANCELLATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 3003, "아임포트 결제 취소 실패"),
    IAMPORT_PAYMENT_COMPLETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 3004, "아임포트 결제 완료 실패"),

    // 주문 관련 에러
    ORDER_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 2001, "주문 생성 실패"),
    ORDER_DETAIL_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 2002, "주문 상세 생성 실패"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, 2003, "주문을 찾을 수 없음"),

    // 환불 관련 에러
    REFUND_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 4001, "환불 요청 생성 실패"),
    REFUND_NOT_FOUND(HttpStatus.NOT_FOUND, 4002, "환불 정보를 찾을 수 없음"),

    // 기타 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "서버 내부 오류");

    private final HttpStatus status;
    private final int code;
    private final String message;
}