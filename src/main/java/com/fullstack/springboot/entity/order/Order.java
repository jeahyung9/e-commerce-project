package com.fullstack.springboot.entity.order;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "prod_order")
public class Order{
	//주문 entity, orderDetail 에서 개별 상품의 정보를 받아 한번에 저장하고 결제 여부를 판단하게 될 것, Member 과 Join
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ono;
	
	@Column(nullable = false)
	@Builder.Default
	private Long o_totalPrice = 0L; // not null
	
	private String o_address; // not null
	
	private String o_reciver; // not null
	
	private String o_reciverPhoNum; // not null
	
	private boolean orderState; // defalutValue = false , 환불은 enum 을 사용할지 아니면 boolean 을 사용할지는 고민 
	
	@CreatedDate
	@Column(name = "orderDate", updatable = false)
	private LocalDateTime orderDate; // 주문 날짜 저장
	
	public void increaseTotalPrice(int price) {
		this.o_totalPrice += price;
	}
	
//	@ElementCollection(fetch = FetchType.LAZY)
//	@Builder.Default
//	private Set<OrderStatus> orderState = new HashSet<OrderStatus>();
//	
//	//권한을 설정하는 메서드 정의
//	public void addMemberRoleSet(OrderStatus orderStatus) {
//		orderState.add(orderStatus);
//	}
//	
//	public void clearRole() {
//		orderState.clear();
//	}
	
	
}
