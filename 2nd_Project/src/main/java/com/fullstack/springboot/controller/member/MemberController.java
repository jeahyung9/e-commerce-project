package com.fullstack.springboot.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Log4j2
public class MemberController {
	
	private final MemberService memberService;
	
	@PostMapping("/login")
	public ResponseEntity<MemberDTO> loginMember(@RequestBody MemberDTO memberDto) {
	    log.error("로그인정보: " + memberDto);
	    try {
	        MemberDTO authenticatedMember = memberService.loginMember(memberDto.getM_email(), memberDto.getM_pw());
	        return ResponseEntity.ok(authenticatedMember);  // 로그인 성공시 응답
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(401).body(null); // 로그인 실패시 오류 응답
	    }
	}
	
	@PostMapping("/join") // 회원가입
	public ResponseEntity<MemberDTO> joinMember(@RequestBody MemberDTO memberDto){
		// 중복 아이디 체크
		boolean isDuplicate = memberService.checkDuplicateId(memberDto.getM_id());
		if (isDuplicate) {
			// ID가 중복될 경우 400 상태 코드와 메시지 반환
			return ResponseEntity.status(400).body(null);
		}
		
		// 중복되지 않으면 회원가입 처리
		MemberDTO createMember = memberService.createMember(memberDto);
		return ResponseEntity.ok(createMember);
	}
	
	@GetMapping("/check-id/{m_id}") // 중복 아이디 체크
    public ResponseEntity<Boolean> checkDuplicateId(@PathVariable String m_id) {
        boolean isDuplicate = memberService.checkDuplicateId(m_id);
        return ResponseEntity.ok(isDuplicate); // 중복 여부 반환
    }
	
	@PostMapping("/ban/{mno}")//아이디벤
    public ResponseEntity<String> banUser(@PathVariable Long mno) {
        memberService.banUser(mno);  // 차단 처리
        return ResponseEntity.ok("User banned successfully.");
    }
	
}