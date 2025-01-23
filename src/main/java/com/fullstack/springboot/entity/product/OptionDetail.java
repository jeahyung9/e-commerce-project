package com.fullstack.springboot.entity.product;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class OptionDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long odno;
	
	private String od_name; //s, m, l, xl(사이즈), red, blue, black, white(색상) 과 딸기맛, 포도맛, 사과맛, 오렌지맛(맛)
	
	private int od_stock; //옵션별 재고
	
	private Long od_price; // 옵션 추가금
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_pno")
	private Product product;
	
}
