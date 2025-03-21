package com.fullstack.springboot.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IamportResponseDTO {

    @JsonProperty("imp_uid")
    private String imp_uid;         // 아임포트 결제번호

    @JsonProperty("merchant_uid")
    private String merchantUid;    // 주문번호

    @JsonProperty("amount")
    private Long amount;           // 결제금액

    @JsonProperty("status")
    private String status;         // 결제상태

    @JsonProperty("success")
    private boolean success;       // 결제성공 여부

    @JsonProperty("code")
    private String code;           // 응답 코드

    @JsonProperty("message")
    private String message;        // 응답 메시지

    @JsonProperty("pay_method")
    private String payMethod;      // 결제 방법

    @JsonProperty("buyer_name")
    private String buyerName;      // 구매자 이름

    @JsonProperty("buyer_tel")
    private String buyerTel;       // 구매자 연락처

    @JsonProperty("buyer_addr")
    private String buyerAddr;      // 구매자 주소

    @JsonProperty("paid_at")
    private String paymentDate;    // 결제 날짜
}