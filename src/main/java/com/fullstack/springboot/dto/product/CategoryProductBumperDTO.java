package com.fullstack.springboot.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryProductBumperDTO {
	
	private Long cpno;
	
	private Long ctno;
	
	private Long pno;
}
