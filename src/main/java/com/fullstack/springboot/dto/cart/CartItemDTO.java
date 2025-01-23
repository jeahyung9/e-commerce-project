package com.fullstack.springboot.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
	
	private Long cino;
	private Long odno;
	private Long pno;
	private Long sno;
	private Long cno;
	private Long mno;
	private String p_name;
	private String od_name;
	private Long p_price;
	private int p_salePer;
	private String businessName;
	private int c_cnt;
	private int p_stock;

}
