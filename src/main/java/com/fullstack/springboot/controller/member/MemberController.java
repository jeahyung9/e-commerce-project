package com.fullstack.springboot.controller.member;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.admin.NoticeDTO;
import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.dto.member.MemberModifyDTO;
import com.fullstack.springboot.service.member.MemberService;
import com.fullstack.springboot.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Log4j2
public class MemberController {
	
	private final MemberService memberService;
	
	@GetMapping("/kakao/suc/{accessToken}")
	public Map<String, Object> getMemberFromKakao(@PathVariable(name = "accessToken") String accessToken){
		log.error("호출됨");
		log.error("요청된 엑세스 토큰 " + accessToken);
		
		try {
			MemberDTO memberDTO = memberService.getKakaoMember(accessToken);
			//아래추가
			String email = memberDTO.getM_email();
			MemberDTO existingMember = memberService.findByEmail(email);
			if(existingMember == null) {
				log.info("회원이 존재하지 않으므로 신규 가입 처리");
				memberService.createMember(memberDTO);
			}
			
			Map<String, Object> claims = memberDTO.getClaims();
			
			String jwtAccessToken = JWTUtil.genToken(claims, 10); // Access Token 생성 (10분)
			String jwtRefreshToken = JWTUtil.genToken(claims, 60*24); //Refresh Token 생성 (24시간)
			
			claims.put("accessToken", jwtAccessToken);
			claims.put("refreshToken", jwtRefreshToken);
			System.out.println(claims.get("accessToken"));
			for (Entry<String, Object> entrySet : claims.entrySet()) {
				System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
			}
			
			return claims;
		} catch (Exception e) {
	        log.error("카카오 로그인 실패: " + e.getMessage());
	        return Map.of("error", "카카오 로그인 처리 중 오류가 발생했습니다.");
	    }
	}

	@PostMapping("/naver/token/{code}")
	public ResponseEntity<String> getToken(@PathVariable("code") String code) {

		String url = "https://nid.naver.com/oauth2.0/token";
		String params = String.format("grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&state=STATE",
        "UwTwxxopvCbJN7NMCtn6", "vFo6vebdiX", code);

		log.error("params : " + params);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<String> entity = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		
		return response;
	}
	
	
	@GetMapping("/naver/suc/{accessToken}")
	public Map<String, Object> getMemberFromnaver1(@PathVariable(name = "accessToken") String accessToken){
		log.error("호출됨");
		log.error("요청된 엑세스 토큰 " + accessToken);
		
		try {
			MemberDTO memberDTO = memberService.getNaverMember(accessToken);
			String email = memberDTO.getM_email();
			MemberDTO existingMember = memberService.findByEmail(email);
			if(existingMember == null) {
				log.info("회원 존재 x 신규 가입 처이");
				memberService.createMember(memberDTO);
			}
			
			Map<String, Object> claims = memberDTO.getClaims();
			
			String jwtAccessToken = JWTUtil.genToken(claims, 10);
			String jwtRefreshToken = JWTUtil.genToken(claims, 60 * 24);
			
			claims.put("accessToken", jwtAccessToken);
			claims.put("refreshToken", jwtRefreshToken);
			System.out.println(claims.get("accessToken"));
			for (Entry<String, Object> entrySet : claims.entrySet()) {
				System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
			}
			return claims;
		}catch (Exception e) {
			log.error("네이버 로그인 실패 : " + e.getMessage());
			return Map.of("error", "네이버 로그인 처리 중 오류 발생");
		}
	}

	@GetMapping("/google/suc/{accessToken}")
	public Map<String, Object> getMemberFromGoogle(@PathVariable("accessToken") String accessToken) {
       
       // 구글에서 사용자 정보를 가져오기 위한 서비스 메서드 호출
       MemberDTO memberDTO = memberService.getGoogleMember(accessToken);
       
       if (memberDTO == null) {
           throw new RuntimeException("회원 정보를 가져올 수 없습니다.");
       }

       // 이메일 정보 추출
       String email = memberDTO.getM_email();
       log.error("받은 이메일: " + email);

       // 사용자 정보에서 claims 추출
       Map<String, Object> claims = memberDTO.getClaims();
       
       // JWT 토큰 생성 (액세스 토큰과 리프레시 토큰)
       String jwtAccessToken = JWTUtil.genToken(claims, 10);  // 10분 유효
       String jwtRefreshToken = JWTUtil.genToken(claims, 60 * 24);  // 24시간 유효
       
       // JWT 토큰을 claims에 추가
       claims.put("accessToken", jwtAccessToken);
       claims.put("refreshToken", jwtRefreshToken);

       // 클라이언트에게 반환
       return claims; // JWT 토큰과 관련된 클레임 반환
   }
	
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
	
	@PutMapping("/modify")
	public ResponseEntity<MemberDTO> modifyMember(@RequestBody MemberDTO memberDTO) {
	    log.info("회원 수정 요청: " + memberDTO);
	    try {
	        MemberDTO updatedMember = memberService.modifyMember(memberDTO);  // 수정된 회원 정보
	        return ResponseEntity.ok(updatedMember);  // 수정된 정보를 반환
	    } catch (Exception e) {
	        return ResponseEntity.status(400).body(null);  // 오류 시 null 반환
	    }
	}
	
	@PutMapping("/mypage")
	public ResponseEntity<MemberDTO> mypage(@RequestBody MemberDTO memberDTO) {
	    log.info("마이페이지: " + memberDTO);
	    try {
	        MemberDTO updatedMember = memberService.modifyMember(memberDTO);  // 수정된 회원 정보
	        return ResponseEntity.ok(updatedMember);  // 수정된 정보를 반환
	    } catch (Exception e) {
	        return ResponseEntity.status(400).body(null);  // 오류 시 null 반환
	    }
	}

	@RequestMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
	    // 로그아웃 처리: 클라이언트에서 전달된 토큰을 무효화하거나 삭제하는 처리
	    String accessToken = request.getHeader("Authorization");
	    if (accessToken != null) {
	        JWTUtil.invalidateToken(accessToken);  // 토큰 무효화
	    }

	    return ResponseEntity.ok().build();
	}
	
	@GetMapping("/info/{email}")
    public ResponseEntity<MemberDTO> getMemberByEmail(@PathVariable("email") String email) {
		
		log.error("email-----------------------------------------");
		
        try {
            MemberDTO member = memberService.findByEmail(email);
            log.error("try---------------------------------------------------");
            return ResponseEntity.ok(member);
        } catch (RuntimeException e) {
        	log.error("throw---------------------------------------------------");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
	
	@GetMapping("/list")
	public PageResponseDTO<MemberDTO> getMemberList(PageRequestDTO pageRequestDTO,
			@RequestParam(value = "isBan", required = false) Boolean isBan,
			@RequestParam(value = "id", required = false) String id){

		log.error(isBan + " : controller");
		
		return memberService.getMemberList(pageRequestDTO, id, isBan);
	}
	
	@PostMapping("/join") // 회원가입
	public ResponseEntity<MemberDTO> createMember(@RequestBody MemberDTO memberDto){
		// 중복 아이디 체크
		System.out.println(memberDto);
		boolean isDuplicate = memberService.checkDuplicateId(memberDto.getM_email());
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