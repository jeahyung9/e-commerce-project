package com.fullstack.springboot.dto.seller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fullstack.springboot.dto.admin.SuperAuth;
import com.fullstack.springboot.dto.product.ProductDTO;
import com.fullstack.springboot.entity.seller.Seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerDTO {
	
	private Long sno;
	
	private String sId;
	
	private String sPw;
	
	private String businessName;
	
	private String businessNum;
	
	private String sNum;
	
	private String sEmail;
	
	private String sAddr;
	
	private Long totalSales = 0L;
	
	private List<ProductDTO> productList;
	
	private Set<SuperAuth> superAuth = new HashSet<>();
	
	public SellerDTO(Seller seller) {
		this.sno = seller.getSno();
		this.sId = seller.getS_id();
		this.sPw = seller.getS_pw();
		this.businessName = seller.getBusinessName();
		this.businessNum = seller.getBusinessNum();
		this.sNum = seller.getS_num();
		this.sEmail = seller.getS_email();
		this.sAddr = seller.getS_addr();
		this.totalSales = seller.getTotalSales();
		this.superAuth = seller.getSuperAuth();
	}
	
}
