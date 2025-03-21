package com.fullstack.springboot.entity.order;

import com.fullstack.springboot.entity.product.OptionDetail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = "order")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order_detail")
public class OrderDetail {
	//주문 상세 entity, 1개의 상품의 정보를 모아옴, Order, Product 테이블과 조인
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orno;
	
	private int or_price;
	
	private int or_count;
	
	@ManyToOne
	@JoinColumn(name = "ono")
	private Order order;
	
//	@Column(nullable = false)
//	private boolean review_flag;
	
    @Column
    private Long rno;  // review_flag 대신 rno로 변경
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "option_detail_odno") // 외래 키 명시
	private OptionDetail optionDetail;
    
}
