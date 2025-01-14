package com.fullstack.springboot.entity.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "order_detail")
public class OrderDetail {
	//주문 상세 entity, 1개의 상품의 정보를 모아옴, Order, Product 테이블과 조인
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long odno;
	
	private Long od_price;
	
	private Long od_count;
	
	@ManyToOne
	//@JoinColumn(name = "order_ono")
	private Order order;
	
//	@ManyToOne
//	@JoinColumn(name="product_pno")
//	private Product product;
}
