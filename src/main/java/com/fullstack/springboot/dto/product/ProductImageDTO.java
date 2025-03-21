package com.fullstack.springboot.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
	
	private Long pino;
	
	private String pi_name; //이미지 이름
	
	private String pi_path; // 이미지 경로
	
	private int pi_ord; // 이미지 순서
	
	private Long pno;// Product
}
