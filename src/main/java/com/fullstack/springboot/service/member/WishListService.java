package com.fullstack.springboot.service.member;

import java.util.List;

import com.fullstack.springboot.dto.member.WishListDTO;
import com.fullstack.springboot.entity.product.WishList;

public interface WishListService {
	
	public List<WishListDTO> getWishList(Long mno);
	
	public void register(WishListDTO wishListDTO);
	
	public List<WishListDTO> remove(Long wno, Long mno);

}
