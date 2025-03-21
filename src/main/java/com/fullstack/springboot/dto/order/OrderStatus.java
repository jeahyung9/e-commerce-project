package com.fullstack.springboot.dto.order;


//주문의 상태. 현재로는 쓰지 않지만 쓰게 될 가능성이 있음. 우선 적으로는 주문과 환불 상태로 구별
public enum OrderStatus {
    ORDER_COMPLETE("주문완료"),    // 결제 완료된 정상 주문
    
    
	REFUND_PENDING("환불 대기"),  // 환불 대기 상태
    REFUND_COMPLETE("환불 완료"), // 환불 완료 상태
	
	CANCEL("취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}