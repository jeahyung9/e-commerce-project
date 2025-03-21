package com.fullstack.springboot.external.payment.exception;

import lombok.Getter;

/**
 * 결제/주문 관련 커스텀 예외 클래스
 */
@Getter
public class PaymentException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    /**
     * 문자열 메시지로 예외를 생성합니다.
     */
    public PaymentException(String message) {
        super(message);
        this.errorCode = ErrorCode.ORDER_CREATION_FAILED; // 기본 에러 코드 설정
    }
    
    /**
     * ErrorCode만으로 예외를 생성합니다.
     */
    public PaymentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode와 추가 메시지로 예외를 생성합니다.
     */
    public PaymentException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode와 원인 예외로 예외를 생성합니다.
     */
    public PaymentException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode, 추가 메시지, 원인 예외로 예외를 생성합니다.
     */
    public PaymentException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}