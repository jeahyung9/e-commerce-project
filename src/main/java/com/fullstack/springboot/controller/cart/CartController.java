package com.fullstack.springboot.controller.cart;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.cart.CartItemDTO;
import com.fullstack.springboot.service.cart.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Log4j2
public class CartController {

	private final CartService cartService;
	
	@GetMapping("/{mno}")
	public List<CartItemDTO> getCartItem(@PathVariable("mno") Long mno){
		log.error("mno : " + mno);
		return cartService.getCartItems(mno);
	}
	
	@PostMapping("/add")
	public List<CartItemDTO> addCartItem(@RequestBody CartItemDTO cartItemDTO){
		return cartService.register(cartItemDTO);
	}
	
	@PutMapping("/change")
	public List<CartItemDTO> changeCartItem(@RequestBody CartItemDTO cartItemDTO){
		
		if(cartItemDTO.getC_cnt() <= 0) {
			return cartService.deleteCartItem(cartItemDTO.getCino(), cartItemDTO.getMno());
		}
		log.error(cartItemDTO);
		return cartService.changeCartItem(cartItemDTO);
	}
	
	@DeleteMapping("/delete/{cino}/{mno}")
	public List<CartItemDTO> deleteItem(@PathVariable("cino") Long cino, @PathVariable("mno") Long mno){
		return cartService.deleteCartItem(cino, mno);
	}
	
	
}
