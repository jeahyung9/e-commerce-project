package com.fullstack.springboot.service.member;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.dto.member.MemberModifyDTO;
import com.fullstack.springboot.entity.member.Member;

public interface MemberService { // 유저 상태 관리 서비스

	MemberDTO createMember(MemberDTO memberDto); //회원가입
	
	boolean checkDuplicateId(String m_id); //아이디 중복체크

	void banUser(Long mno);
	
	void unbanUser(Long mno);
	
	public MemberDTO loginMember(String m_email, String password);
	
	MemberDTO getMemberInfo(String email);
	
	PageResponseDTO<MemberDTO> getMemberList(PageRequestDTO pageRequestDTO, String id, Boolean isBan);

	MemberDTO modifyMember(MemberDTO memberDTO);

	MemberDTO findByEmail(String email);
	
	MemberDTO mypage(String email);
	
	MemberDTO findByMno(Long mno);
	
	MemberDTO getKakaoMember(String accessToken);
	
	MemberDTO getNaverMember(String accessToken);

	MemberDTO getGoogleMember(String accessToken);

	
	default MemberDTO entityToDTO(Member member) {
		
		MemberDTO dto = new MemberDTO(
				member.getMno(),
				member.getM_pw(),
				member.getM_name(),
				member.getM_nickName(),
				member.getM_phoNum(),
				member.getM_email(),
				member.getDef_addr(),
				member.getBirth(),
				member.getIsMan(),
				member.isAd_agree(),
				member.isInfo_agree(),
				member.isBan(),
				member.getTotalPay(),
				member.getMembership(),
				member.isFormSns()
				);
				
		return dto;
	}

}