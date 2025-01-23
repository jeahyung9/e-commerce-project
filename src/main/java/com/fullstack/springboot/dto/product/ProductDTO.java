package com.fullstack.springboot.dto.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	
	private Long pno;
	
	private String p_name;
	
	private String p_content;
	
	private Long p_price;
	
	private int p_salePer;
	
	private int p_stock;
	
	private int p_salesVol;
	
	private Long sno;
	
	private boolean delFlag;
	
//	private List<ProductImageDTO> productImage;
	
	private List<OptionDetailDTO> optionDetails;
	
	//내가 추가한 코드 (임시)
	private String businessName;
}
