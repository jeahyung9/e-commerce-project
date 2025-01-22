package com.fullstack.springboot.service;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.dto.member.MemberModifyDTO;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	@Autowired
    MemberRepository memberRepository;
	
    private final PasswordEncoder passwordEncoder;  // 생성자 주입 방식으로 변경

//    @Autowired
//    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
//        this.memberRepository = memberRepository;
//        this.passwordEncoder = passwordEncoder;  // 주입
//    }

    @Override
    public MemberDTO createMember(MemberDTO memberDto) {
    	System.out.println("암호화 " + passwordEncoder);
    	// ID 중복 체크
        if (checkDuplicateId(memberDto.getM_id())) {
            throw new RuntimeException("이미 존재하는 ID입니다.");
        }

        // 이메일 중복 체크
        if (memberRepository.findByM_email(memberDto.getM_email()) != null) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        String encodedPassword = passwordEncoder.encode(memberDto.getM_pw());
        System.out.println(encodedPassword);
        
        Member member = Member.builder()
                .m_id(memberDto.getM_id())
                .m_pw(encodedPassword)
                .m_name(memberDto.getM_name())
                .m_nickName(memberDto.getM_nickname())
                .m_phoNum(memberDto.getM_phoNum())
                .m_email(memberDto.getM_email())
                .def_addr(memberDto.getDef_addr())
                .birth(memberDto.getBirth())
                .isMan(memberDto.isMan())
                .ad_agree(memberDto.isAd_agree())
                .info_agree(memberDto.isInfo_agree())
                .totalPay(memberDto.getTotalPay())
                .membership(memberDto.getMembership())
                .build();
        
        memberRepository.save(member);
        return memberDto;
    }

    @Override
    public boolean checkDuplicateId(String m_id) {
        return memberRepository.getMemberInfo(m_id) != null;  // 아이디가 이미 존재하는지 확인
    }

    public void banUser(Long mno) {
        Member member = memberRepository.findById(mno).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));
        member.banUser();
        memberRepository.save(member);
    }

    public void unbanUser(Long mno) {
        Member member = memberRepository.findById(mno).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));
        member.unbanUser();
        memberRepository.save(member);
    }

    @Override
    public MemberDTO loginMember(String email, String password) {
    	log.error("로그인 이메일 " + email);
        // 이메일로 회원 찾기
        Member member = memberRepository.findByM_email(email);

        log.debug("DB 비번 : " + member.getM_pw());
        log.debug("입력 비번 : " + password);
        
        // 회원이 없으면 예외 처리
        if (member == null) {
            throw new RuntimeException("User not found");
        }

        // 비밀번호 일치 여부 체크
        if (!passwordEncoder.matches(password, member.getM_pw())) {
            throw new RuntimeException("Incorrect password");
        }
        

        // 로그인 성공 시 MemberDTO 반환
        return new MemberDTO(member);
    }

    @Override
    public MemberDTO modifyMember(MemberModifyDTO memberModifyDTO) {
        // 이메일을 이용해서 회원을 찾기
        Member member = memberRepository.findByM_email(memberModifyDTO.getM_email());

        if (member == null) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        // 비밀번호 수정
        if (memberModifyDTO.getM_pw() != null && !memberModifyDTO.getM_pw().isEmpty()) {
            member.setPw(passwordEncoder.encode(memberModifyDTO.getM_pw()));
        }

        // 닉네임 수정
        if (memberModifyDTO.getM_nickname() != null) {
            member.setM_nickName(memberModifyDTO.getM_nickname());
        }
        // 전화번호 수정
        if (memberModifyDTO.getM_phoNum() != null) {
            member.setM_phoNum(memberModifyDTO.getM_phoNum());
        }
        // 주소 수정
        if (memberModifyDTO.getDef_addr() != null) {
            member.setM_def_addr(memberModifyDTO.getDef_addr());
        }

        // 수정된 정보를 저장
        memberRepository.save(member);

        // 수정된 정보를 MemberDTO로 반환
        return new MemberDTO(member);
    }

	@Override
	public MemberDTO getMemberInfo(String email) {
		Member member = memberRepository.findByM_email(email);
		
		if (member == null) {
	        throw new RuntimeException("User not found");
	    }
		
		return new MemberDTO(member);
	}

	@Override
	public MemberDTO findByEmail(String email) {
	    Member member = memberRepository.findByM_email(email);
	    
	    if (member == null) {
	        throw new RuntimeException("User not found");
	    }

	    // MemberDTO로 변환해서 반환
	    return new MemberDTO(member);
	}

	@Override
	public MemberDTO mypage(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	

        
//        return new MemberDTO(
//                member.getMno(),
//                member.getM_id(),
//                member.getM_pw(),
//                member.getM_name(),
//                member.getM_nickName(),
//                member.getM_phoNum(),
//                member.getM_email(),
//                member.getDef_addr(),
//                member.getBirth(),
//                member.isMan(),
//                member.isAd_agree(),
//                member.isInfo_agree(),
//                member.isBan(),
//                member.getTotalPay(),
//                member.getMembership().stream()
//                    .map(membership -> membership.name()) 
//                    .collect(Collectors.toSet())
//            );
    
   
    }