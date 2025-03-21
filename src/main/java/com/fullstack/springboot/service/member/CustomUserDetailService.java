package com.fullstack.springboot.service.member;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.admin.AdminAuthDTO;
import com.fullstack.springboot.dto.member.MemberAuthDTO;
import com.fullstack.springboot.dto.seller.SellerAuthDTO;
import com.fullstack.springboot.entity.admin.Admin;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.seller.Seller;
import com.fullstack.springboot.repository.admin.AdminRepository;
import com.fullstack.springboot.repository.member.MemberRepository;
import com.fullstack.springboot.repository.seller.SellerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	PasswordEncoder passwordEncoder;
	
	private final MemberRepository memberRepository;
	
	private final AdminRepository adminRepository;
	
	private final SellerRepository sellerRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserbyUserName " + username);
		
		Admin admin = adminRepository.getAdminWithId(username);
		if(admin != null) {
			System.out.println(admin);
			return new AdminAuthDTO(admin);
		}
		
		Seller seller = sellerRepository.findBySellerWithSId(username);
		if(seller != null) {
			System.out.println("seller" + seller);
			return new SellerAuthDTO(seller);
		}
		
		Member member = memberRepository.findByM_email(username);
		if(member != null) {
			System.out.println(member);
			return new MemberAuthDTO(member);
		}
		
		throw new UsernameNotFoundException("Missing Username : " + username);
		 
	}

}