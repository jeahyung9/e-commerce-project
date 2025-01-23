package com.fullstack.springboot.controller.wishlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.member.WishListDTO;
import com.fullstack.springboot.service.member.WishListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pick")
public class WishListController {
	
	@Autowired
	private final WishListService wishListService;
	
	@GetMapping("/list/{mno}")
	public List<WishListDTO> getWishList(@PathVariable("mno") Long mno) {
		return wishListService.getWishList(mno);
	}
	
	@PostMapping("/add/{mno}/{wno}")
	public void register(@PathVariable("mno") Long mno, @PathVariable("wno") Long wno) {
		wishListService.register(null);
	}
	
	@DeleteMapping("/remove/{wno}/{mno}")
	public List<WishListDTO> remove(@PathVariable("wno") Long wno, @PathVariable("mno") Long mno) {
		return wishListService.remove(wno, mno);
	}

}
