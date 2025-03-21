package com.fullstack.springboot.dto.order;

import java.time.LocalDateTime;
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
public class RefundDTO {
    private Long rfno;              // 환불번호
    private Long rf_payment;        // 환불금액
    private String rf_reason;       // 환불사유
    private boolean rf_status;      // 환불상태
    
    private LocalDateTime rf_requestDate;  // 환불요청일
    private LocalDateTime rf_completeDate; // 환불완료일
    private Long pmno;              // 결제번호(FK) - 필드 이름 변경
    
    private String imp_uid;
    private String merchant_uid;
    
    
    
}