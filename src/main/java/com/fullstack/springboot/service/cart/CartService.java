package com.fullstack.springboot.service.cart;

import java.util.List;

import com.fullstack.springboot.dto.cart.CartDTO;
import com.fullstack.springboot.dto.cart.CartItemDTO;

public interface CartService {

	public List<CartItemDTO> register(CartItemDTO cartItemDTO);
	
	public List<CartItemDTO> changeCartItem(CartItemDTO cartItemDTO);

	public List<CartItemDTO> getCartItems(Long mno);
	
	public void CartRemove(Long mno);
	
	public List<CartItemDTO> deleteCartItem(Long cino, Long mno);
}
