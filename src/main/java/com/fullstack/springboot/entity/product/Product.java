package com.fullstack.springboot.entity.product;

import com.fullstack.springboot.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name="product")
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pno;
	
	@Column(nullable = false)
	private String p_name; // 이름
	
	private String p_content; // 내용
	
	private String p_img; // 이미지
	
	@Column(nullable = false)
	private Long p_price; // 가격
	
	private int p_salePer; // 할인율
	
	@Column(nullable = false)
	private int p_stock; // 재고
	
	private int p_salesVol; // 판매량
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Seller seller; // 판매자 

	
//	@OneToMany(fetch = FetchType.LAZY)
//	private ProductImage productImage;
	
//	@CreatedDate
//	@Column(name = "regdate", updatable = false)
//	private LocalDateTime regDate;//신규 방명록 글의 등록시간.
	
}