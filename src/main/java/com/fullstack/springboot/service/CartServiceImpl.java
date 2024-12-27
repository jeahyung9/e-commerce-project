package com.fullstack.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.CartDTO;
import com.fullstack.springboot.dto.CartItemDTO;
import com.fullstack.springboot.entity.cart.Cart;
import com.fullstack.springboot.entity.cart.CartItem;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.repository.CartItemRepository;
import com.fullstack.springboot.repository.CartRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Override
	public List<CartItemDTO> getCartItems(Long mno) {
		List<CartItemDTO> cartItems = cartItemRepository.getCartItems(mno);
		return cartItems;
	}
	
	@Override
	public List<CartItemDTO> register(CartItemDTO cartItemDTO) {
		
		Long mno = cartItemDTO.getMno();
		Long pno = cartItemDTO.getPno();
		Long cno = cartItemDTO.getCno();
		int cnt = cartItemDTO.getC_cnt();
		
		Optional<Cart> cartList = cartRepository.getCart(mno);
		
		Cart cart = null;
		
		Product product = Product.builder().pno(pno).build();
		
		//카트 생성 여부
		if(cartList.isEmpty()) {
			Member member = Member.builder().mno(mno).build();
			cart = Cart.builder().member(member).build();
			cartRepository.save(cart);
		}else {
			cart = Cart.builder().cno(cno).build();
		}
		
		//동일한 물품이 카트에 존재하는지 여부
		CartItem cartItem = cartItemRepository.getCi(mno, pno);
		
		if(cartItem == null) {
			log.info("동일한 품목 없음");
			cartItem = CartItem.builder()
					.c_cnt(cnt)
					.cart(cart)
					.product(product)
					.build();
		}else {
			log.info("동일한 품목 있음 : " + cnt + " " + cartItem.getC_cnt() + " = " + cnt + cartItem.getC_cnt());
			cartItem.changeCnt(cnt);
		}
		
		
		cartItemRepository.save(cartItem);
		
		return getCartItems(mno);
	}
	
	@Override
	public void CartRemove(Long mno) {
		
	}
	
	@Override
	public List<CartItemDTO> deleteCartItem(Long cino, Long mno) {
		
		cartItemRepository.deleteById(cino);
		
		return cartItemRepository.getCartItems(mno);
	}

}
