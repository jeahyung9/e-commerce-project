package com.fullstack.springboot.entity.product;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

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
import lombok.ToString;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="product_option_bumper")
public class ProductOptionBumper {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long opno;
	
	private boolean op_status; //이 옵션의 판매 상태
	
	private Long op_price; // 옵션 추가금
	
	private int op_stock; // 옵션별 재고
	
	@CreatedDate
	@Column(name = "op_regDate", updatable = false)
	private LocalDateTime op_regDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_pno")
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_ono")
	private Option option;
}
