package com.fullstack.springboot.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.member.MemberAuthDTO;
import com.fullstack.springboot.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserbyUserName " + username);
		MemberAuthDTO dto = new MemberAuthDTO(memberRepository.findByM_email(username));
		System.out.println("---------------dto " + dto);
		return dto;
		 
	}

}