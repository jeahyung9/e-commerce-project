package com.fullstack.springboot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.CartItemDTO;
import com.fullstack.springboot.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Log4j2
public class CartController {

	private final CartService cartService;
	
	@GetMapping("/items/{mno}")
	public List<CartItemDTO> getCartItem(@PathVariable("mno") Long mno){
		log.error("mno : " + mno);
		return cartService.getCartItems(mno);
	}
	
	@PostMapping("/add")
	public List<CartItemDTO> addCartItem(@RequestBody CartItemDTO cartItemDTO){
		return cartService.register(cartItemDTO);
	}
	
	@PostMapping("/items/change")
	public List<CartItemDTO> changeCartItem(@RequestBody CartItemDTO cartItemDTO){
		return null;
	}
	
	@DeleteMapping("/items/delete/{mno}/{cino}")
	public List<CartItemDTO> deleteItem(@PathVariable("cino") Long cino, @PathVariable("mno") Long mno){
		return cartService.deleteCartItem(cino, mno);
	}
	
	
}
