package com.fullstack.springboot.dto.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class OrderDTO {
    private Long ono;                  // 주문번호
    private Long o_totalPrice;         // 총 가격
    private String o_address;          // 배송주소
    private String o_reciver;          // 수령인
    private String o_reciverPhoNum;    // 수령인 연락처
    //private boolean orderState;        // 주문상태
    
    private OrderStatus status;        // 주문 상태 (OrderStatus enum 사용)
    
    private LocalDateTime orderDate;   // 주문일자
    
    private Long mno;                  // 회원번호 추가
    private List<OrderDetailDTO> orderDetails = new ArrayList<>(); // 주문 상세 정보 추가
    private RefundDTO refundDTO;		// 환불 정보 추가
}