package com.fullstack.springboot.service.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.member.WishListDTO;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.product.WishList;
import com.fullstack.springboot.repository.member.WishListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

	@Autowired
	private WishListRepository wishListRepository;
	
	@Override
	public List<WishListDTO> getWishList(Long mno) {
		return wishListRepository.getWishList(mno);
	}
	
	@Override
	public Long checkWichList(Long mno, Long pno) {
		WishList wishList = wishListRepository.getWishListOne(mno, pno);
		
		if(wishList == null) {
			return null;
		}else {
			return wishList.getWno();
		}
	}
	
	@Override
	public void register(Long mno, Long pno) {
		System.out.println("이거 나오냐");
		WishList wishList = wishListRepository.getWishListOne(mno, pno);
		System.out.println(mno + " : " + pno);
		System.out.println("wishList : " + wishList);
		if(wishList != null) {
			return;
		}
		
		Member member = Member.builder().mno(mno).build();
		Product product = Product.builder().pno(pno).build();
		wishList = WishList.builder().member(member).product(product).build();
		wishListRepository.save(wishList);
	}
	
	@Override
	public List<WishListDTO> remove(Long wno, Long mno) {
		wishListRepository.deleteById(wno);
		return wishListRepository.getWishList(mno);
	}
}
