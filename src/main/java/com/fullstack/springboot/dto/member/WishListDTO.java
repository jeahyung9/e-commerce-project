package com.fullstack.springboot.dto.member;

import com.fullstack.springboot.dto.cart.CartItemDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishListDTO {

	private Long wno;
	private Long pno;
	private Long sno;
	private Long mno;
	private String p_name;
	private Long p_price;
	private int p_salePer;
	private String businessName;
	private int p_stock;
}
