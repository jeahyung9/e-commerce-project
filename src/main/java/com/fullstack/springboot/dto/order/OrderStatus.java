package com.fullstack.springboot.dto.order;

//주문의 상태. 현재로는 쓰지 않지만 쓰게 될 가능성이 있음. 우선 적으로는 주문과 환불 상태로 구별
public enum OrderStatus {
	ORDERED, REFUND
}