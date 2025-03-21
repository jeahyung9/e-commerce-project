package com.fullstack.springboot.service.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.cart.CartDTO;
import com.fullstack.springboot.dto.cart.CartItemDTO;
import com.fullstack.springboot.entity.cart.Cart;
import com.fullstack.springboot.entity.cart.CartItem;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.repository.cart.CartItemRepository;
import com.fullstack.springboot.repository.cart.CartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService {
	
	private final CartRepository cartRepository;

	private final CartItemRepository cartItemRepository;
	
	@Override
	public List<CartItemDTO> getCartItems(Long mno) {
		List<CartItemDTO> cartItems = cartItemRepository.getCartItems(mno);
		return cartItems;
	}
	
	@Override
	public CartItemDTO getCartItemOne(Long mno, Long cino) {
		return cartItemRepository.getCartItemOne(mno, cino);
	}
	
	@Override
	@Transactional
	public List<CartItemDTO> register(CartItemDTO cartItemDTO) {
		
		Long mno = cartItemDTO.getMno();
		Long odno = cartItemDTO.getOdno();
		Long cno = null;
		int cnt = cartItemDTO.getC_cnt();
		
		Optional<Cart> cartList = cartRepository.getCart(mno);
		
		Cart cart = null;
		
		OptionDetail optionDetail = OptionDetail.builder().odno(odno).build();
		
		//카트 생성 여부
		if(cartList.isEmpty()) {
			Member member = Member.builder().mno(mno).build();
			cart = Cart.builder().member(member).build();
			cartRepository.save(cart);
		}else {
			cno = cartList.get().getCno();
			cart = Cart.builder().cno(cno).build();
		}
		
		//동일한 물품이 카트에 존재하는지 여부
		CartItem cartItem = cartItemRepository.getCi(mno, odno);
		
		log.info(cartItem + "이후 확인");
		if(cartItem == null) {
			log.info("동일한 품목 없음");
			cartItem = CartItem.builder()
					.c_cnt(cnt)
					.cart(cart)
					.optionDetail(optionDetail)
					.build();
		}else {
			log.info("동일한 품목 있음 : " + cnt + " " + cartItem.getC_cnt() + " = " + cnt + cartItem.getC_cnt());
			cartItem.changeCnt(cartItem.getC_cnt() + cnt);
		}
		
		log.info(cartItem + "이후 확인");
		
		cartItemRepository.save(cartItem);
		
		return getCartItems(mno);
	}
	
	@Override
	public List<CartItemDTO> changeCartItem(CartItemDTO cartItemDTO) {
		CartItem cartItem = cartItemRepository.getCi(cartItemDTO.getMno(), cartItemDTO.getOdno());
		cartItem.changeCnt(cartItemDTO.getC_cnt());
		cartItemRepository.save(cartItem);
		return getCartItems(cartItemDTO.getMno());
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
