package com.fullstack.springboot.dto.order;

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
public class PaymentDTO {
    private Long pmno;              // 결제번호
    private String pm_method;       // 결제수단
    
    private Long amount; // 결제 금액 추가
    
    private String imp_uid; // 아임포트 결제번호 추가
    
    private String merchant_uid; // 상점 거래번호 추가
    
    private Long ono;         // 주문번호(FK)
}