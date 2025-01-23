package com.fullstack.springboot;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fullstack.springboot.dto.member.WishListDTO;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.product.WishList;
import com.fullstack.springboot.repository.member.WishListRepository;
import com.fullstack.springboot.service.member.WishListService;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class WishListTest {

	@Autowired
	private WishListRepository wishListRepository;
	
	@Autowired
	private WishListService wishListService;

	//@Test
	void insert() {
		IntStream.rangeClosed(1, 5).forEach(i -> {
			WishListDTO dto = WishListDTO.builder().mno(10L).pno((long)((Math.random() * 100) + 1)).build();
			wishListService.register(dto);
		});
	}
	
	//@Test
	void get() {
		List<WishListDTO> list = wishListService.getWishList(10L);
		
		for(WishListDTO dto : list) {
			log.error(dto);
		}
	}
	
	//@Test
	void getOne() {
		WishList list = wishListRepository.getWishListOne(10L, 15L);
		log.error(list.getWno() + " dd " + list.getMember().getMno());
	}
	
	//@Test
	void remove() {
		List<WishListDTO> list = wishListService.remove(4L, 10L);
	}
	
}
