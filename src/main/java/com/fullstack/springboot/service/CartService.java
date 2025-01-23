package com.fullstack.springboot.service;

import java.util.List;

import com.fullstack.springboot.dto.CartDTO;
import com.fullstack.springboot.dto.CartItemDTO;

public interface CartService {

	public List<CartItemDTO> register(CartItemDTO cartItemDTO);

	public List<CartItemDTO> getCartItems(Long mno);
	
	public void CartRemove(Long mno);
	
	public List<CartItemDTO> deleteCartItem(Long cino, Long mno);
}
