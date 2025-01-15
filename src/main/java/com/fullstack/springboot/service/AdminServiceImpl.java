package com.fullstack.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	MemberRepository memberRepository;
	
	@Override
	public void banUser(Long mno) {
		Member member = memberRepository.findById(mno).orElseThrow(()-> new RuntimeException("계정을 찾을수 없음"));
		
		member.banUser(); //isBan 값 true
		memberRepository.save(member);
	}

	@Override
	public void unBanUser(Long mno) {
		Member member = memberRepository.findById(mno).orElseThrow(()-> new RuntimeException("계정을 찾을수 없음"));
		
		member.unbanUser(); // isBan 값 false
		memberRepository.save(member);
		
	}
	
	

}
