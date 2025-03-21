package com.fullstack.springboot.entity.product;

import com.fullstack.springboot.entity.BaseEntity;

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
@Table(name = "product_img")
public class ProductImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pino;
	
	private String pi_name; // 이미지 이름
	
	private String pi_path; // 이미지 경로
	
	private int pi_ord; // 이미지 순서
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

}
