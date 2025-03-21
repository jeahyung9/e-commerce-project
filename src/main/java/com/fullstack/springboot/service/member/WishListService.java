package com.fullstack.springboot.service.member;

import java.util.List;

import com.fullstack.springboot.dto.member.WishListDTO;
import com.fullstack.springboot.entity.product.WishList;

public interface WishListService {
	
	public List<WishListDTO> getWishList(Long mno);
	
	public Long checkWichList(Long mno, Long pno);
	
	public void register(Long mno, Long pno);
	
	public List<WishListDTO> remove(Long wno, Long mno);

}
