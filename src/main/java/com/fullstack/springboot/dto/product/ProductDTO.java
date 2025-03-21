package com.fullstack.springboot.dto.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fullstack.springboot.entity.product.CategoryProductBumper;

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
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class ProductDTO {
	
	private Long pno;
	
	@JsonProperty("pName")
	private String p_name;
	
	@JsonProperty("pContent")
	private String p_content;
	
	@JsonProperty("pPrice")
	private Long p_price;
	
	@JsonProperty("pSalePer")
	private int p_salePer;
	
	@JsonProperty("pStock")
	private int p_stock;
	
	@JsonProperty("pSalesVol")
	private int p_salesVol;
	
	private Long sno;
	
	private String businessName;
	
	private String s_email;
	
	private boolean delFlag;
	
	private List<ProductImageDTO> productImage;
	
	private List<OptionDetailDTO> optionDetail;
	
	private List<CategoryProductBumperDTO> categoryProductBumpers;
	
}
