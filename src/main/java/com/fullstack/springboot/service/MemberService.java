package com.fullstack.springboot.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.dto.member.MemberModifyDTO;

public interface MemberService { // 유저 상태 관리 서비스

	MemberDTO createMember(MemberDTO memberDto); //회원가입
	
	boolean checkDuplicateId(String m_id); //아이디 중복체크

	void banUser(Long mno);
	
	void unbanUser(Long mno);
	
	public MemberDTO loginMember(String m_email, String password);
	
	MemberDTO getMemberInfo(String email);

	MemberDTO modifyMember(MemberModifyDTO memberModifyDTO);

	MemberDTO findByEmail(String email);
	
	MemberDTO mypage(String email);
	

}