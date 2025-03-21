package com.fullstack.springboot.service.seller;

import com.fullstack.springboot.dto.seller.SellerDTO;

public interface SellerService {
	
	SellerDTO createSeller(SellerDTO sellerDTO); // 회원가입
	
	boolean checkDuplicateSellerId(String sId);
	
	SellerDTO loginSeller(String sId, String sPw);
}
