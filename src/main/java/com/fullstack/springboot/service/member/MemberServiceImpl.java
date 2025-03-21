package com.fullstack.springboot.service.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.dto.member.MemberModifyDTO;
import com.fullstack.springboot.dto.member.Membership;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.repository.member.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;



@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;
	
    private final PasswordEncoder passwordEncoder;  // 생성자 주입 방식으로 변경
    
    @Override
	public MemberDTO getKakaoMember(String accessToken) {
		log.error("호출될까?");
		
		Map<String, String> userInfo = getEmailFromKakaoAccessToken(accessToken);
		String email = userInfo.get("email");
		String nickname = userInfo.get("nickname");
		
		log.error("email : " + email);
		log.error("nickname : " + nickname);
		
		Optional<Member> result = memberRepository.findBykakao(email);
		
		if(result.isPresent()) {
			System.out.println("있음");
			MemberDTO memberDTO = entityToDTO(result.get());
			return memberDTO;
		}
		
		Member socialMember = makeSocialMember(email, nickname);
		memberRepository.save(socialMember);
		
		MemberDTO memberDTO = entityToDTO(socialMember);
		
		return memberDTO;
	}
    
    private Map<String, String> getEmailFromKakaoAccessToken(String accessToken) {
		String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";
		
		if(accessToken == null) {
			throw new RuntimeException("카카오 인증 엑세스 토큰 없음");
		}
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		HttpEntity<Object> httpEntity = new HttpEntity(headers);
		
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();
		ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
				uriComponents.toString(),
				HttpMethod.GET,
				httpEntity,
				LinkedHashMap.class);
		
		LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
		log.error("카카오 요청 결과 : " + bodyMap);
		
		LinkedHashMap<String, Object> kakaoAccount = bodyMap.get("kakao_account");
		log.error("카카오 계정 : " + kakaoAccount);
		
		LinkedHashMap<String, Object> profile = (LinkedHashMap<String, Object>) kakaoAccount.get("profile");

		String email = (String) kakaoAccount.get("email");
		String nickname = "";

		if(profile != null) {
			nickname = (String) profile.get("nickname");
		}else{
			nickname = "소셜회원";
		}
		
		System.out.println(email + " +++ " + nickname);
		
		if (email == null) {
			throw new RuntimeException("카카오 이메일 정보 x");
		}
		
		Member member = makeSocialMember(email, nickname);
		
		Map<String, String> result = new HashMap<>();
		result.put("email", email);
		result.put("nickname", nickname);
		
		return result;
	}
    
    @Override
	public MemberDTO getNaverMember(String accessToken) {
		log.error("네이버 호출");
		
		Map<String, String> userInfo = getEmailFromNaverAccessToken(accessToken);
		String email = userInfo.get("email");
		String nickname = userInfo.get("nickname");
		String name = userInfo.get("name");
		String phoNum = userInfo.get("phoNum");
		
		log.error("email : " + email);
		log.error("nickname : " + nickname);
		
		Optional<Member> result = memberRepository.findByNaver(email);
		
		if(result.isPresent()) {
			System.out.println("네이버있음");
			MemberDTO memberDTO = entityToDTO(result.get());
			return memberDTO;
		}
		Member socialMember = makeSocialMember(email, nickname);
		memberRepository.save(socialMember);
		
		MemberDTO memberDTO = entityToDTO(socialMember);
		return memberDTO;
	}
	
	private Map<String, String> getEmailFromNaverAccessToken(String accessToken) {
		String NaverGetUserURL = "https://openapi.naver.com/v1/nid/me";
		
		if(accessToken == null) {
			throw new RuntimeException("네이버 인증 엑세스 토큰 없음");
		}
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		HttpEntity<Object> httpEntity = new HttpEntity(headers);
		
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(NaverGetUserURL).build();
		

		ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
				uriComponents.toString(),
				HttpMethod.GET,
				httpEntity,
				LinkedHashMap.class);
		
		LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
		log.error("네이버 요청 결과 : " + bodyMap);
		
		LinkedHashMap<String, Object> naverAccount = bodyMap.get("response");
		log.error("네이버 계정 : " + naverAccount);
		
		//LinkedHashMap<String, Object> profile = (LinkedHashMap<String, Object>) naverAccount.get("profile");
		
		String email = (String) naverAccount.get("email");
		String nickname = (String) naverAccount.get("nickname");
		String name = (String) naverAccount.get("name");
		String phoNum = (String) naverAccount.get("mobile");
		
		System.out.println("naver : " + email + " +++ " + "naver : " + nickname);
		
		if (email == null) {
			throw new RuntimeException("네이버 이메일 정보 x");
		}
		
		Member member = makeSocialMember(email, nickname);
		
		Map<String, String> result = new HashMap<>();
		result.put("email", email);
		result.put("nickname", nickname);
		result.put("name", name);
		result.put("phoNum", phoNum);
		
		return result;
	}

	//구글 액세스 토큰을 처리하는 메서드 추가
	@Override
	public MemberDTO getGoogleMember(String accessToken) {

		String googleUserInfoURL = "https://www.googleapis.com/oauth2/v3/userinfo";

		// 구글 API로 사용자 정보 요청
		RestTemplate restTemplate = new RestTemplate();

		// 구글 API 호출 (사용자 정보 받기)
		String url = googleUserInfoURL + "?access_token=" + accessToken;
		ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);

		Map<String, Object> userInfo = responseEntity.getBody();
		System.out.println(userInfo);

		if (userInfo == null) {
			throw new RuntimeException("구글 사용자 정보 요청에 실패했습니다.");
		}

		String email = (String) userInfo.get("email");
		String name = (String) userInfo.get("name");

		Optional<Member> result = memberRepository.findByGoogle(email);
		
		if(result.isPresent()) {
			System.out.println("구글 있음");
			MemberDTO memberDTO = entityToDTO(result.get());
			return memberDTO;
		}
		Member socialMember = makeSocialMember(email, name);
		memberRepository.save(socialMember);
		
		MemberDTO memberDTO = entityToDTO(socialMember);
		return memberDTO;
	}

   // 구글 액세스 토큰을 통해 이메일을 추출하는 메서드
   private String getEmailFromGoogleAccessToken(String accessToken) {
       String googleUserInfoURL = "https://www.googleapis.com/oauth2/v3/userinfo";

       if (accessToken == null) {
           throw new RuntimeException("구글 인증 엑세스 토큰 없음");
       }

       RestTemplate restTemplate = new RestTemplate();

       HttpHeaders headers = new HttpHeaders();
       headers.add("Authorization", "Bearer " + accessToken); // 액세스 토큰 포함
       headers.add("Content-Type", "application/x-www-form-urlencoded");
       HttpEntity<String> httpEntity = new HttpEntity<>(headers);

       UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(googleUserInfoURL).build();
       ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, httpEntity, LinkedHashMap.class);
       LinkedHashMap<String, String> bodyMap = response.getBody();

       log.error("--------------------");
       log.error("구글 요청 결과: " + bodyMap);

       // 구글에서 반환된 사용자 정보에서 이메일 추출
       return bodyMap.get("email");
   }
	
	private String makeTempPassword() {
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < 10; i++) {
			buffer.append((char) ((int) (Math.random() * 55) + 65));
		}
		return buffer.toString();
	}
	
	private Member makeSocialMember(String email, String nickname) {
		String tempPassword = makeTempPassword();
		
		log.info("tempPassword : " + tempPassword);
		
		System.out.println(email);
		Member member = Member.builder().isMan(true).m_email(email).m_nickName(nickname).m_pw(passwordEncoder.encode(tempPassword)).m_name(nickname)
				.formSns(true).build();
//		Member member = Member.builder().isMan(true).m_email(email).m_nickName(nickname).m_phoNum("000000").m_pw(passwordEncoder.encode(tempPassword)).m_name(nickname)
//				.formSns(true).build();
		
		member.addMembershipSet(Membership.USER);
		
		return member;
	}

    @Override
    public MemberDTO createMember(MemberDTO memberDto) {
    	System.out.println("암호화 " + passwordEncoder);
    	// ID 중복 체크
//        if (checkDuplicateId(memberDto.getM_id())) {
//            throw new RuntimeException("이미 존재하는 ID입니다.");
//        }

        // 이메일 중복 체크
        if (memberRepository.findByM_email(memberDto.getM_email()) != null) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        String encodedPassword = passwordEncoder.encode(memberDto.getM_pw());
        System.out.println(encodedPassword);
        
        Member member = Member.builder()
                .m_pw(encodedPassword)
                .m_name(memberDto.getM_name())
                .m_nickName(memberDto.getM_nickname())
                .m_phoNum(memberDto.getM_phoNum())
                .m_email(memberDto.getM_email())
                .def_addr(memberDto.getDef_addr())
                .birth(memberDto.getBirth())
                .isMan(memberDto.getIsMan())
                .ad_agree(memberDto.isAd_agree())
                .info_agree(memberDto.isInfo_agree())
                .totalPay(memberDto.getTotalPay())
                .membership(memberDto.getMembership())
                .build();
        
        memberRepository.save(member);
        return memberDto;
    }

    @Override
    public boolean checkDuplicateId(String m_email) {
        return memberRepository.getMemberInfo(m_email) != null;  // 아이디가 이미 존재하는지 확인
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
    public MemberDTO modifyMember(MemberDTO memberDTO) {
        // 이메일을 이용해서 회원을 찾기
        Member member = memberRepository.findByM_email(memberDTO.getM_email());
        System.out.println(member);

        if (member == null) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        // 비밀번호 수정
        if (memberDTO.getM_pw() != null && !memberDTO.getM_pw().isEmpty()) {
            member.setPw(passwordEncoder.encode(memberDTO.getM_pw()));
        }
        
        // 네임 수정
        if (memberDTO.getM_name() != null) {
            member.setM_name(memberDTO.getM_name());
        }

        // 닉네임 수정
        if (memberDTO.getM_nickname() != null) {
            member.setM_nickName(memberDTO.getM_nickname());
        }
        // 전화번호 수정
        if (memberDTO.getM_phoNum() != null) {
            member.setM_phoNum(memberDTO.getM_phoNum());
        }
        // 주소 수정
        if (memberDTO.getDef_addr() != null) {
            member.setM_def_addr(memberDTO.getDef_addr());
        }

        System.out.println(member);
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
	public PageResponseDTO<MemberDTO> getMemberList(PageRequestDTO pageRequestDTO, String id, Boolean isBan) {
		
		Pageable pageable = 
				PageRequest.of(
						pageRequestDTO.getPage() -1, 
						pageRequestDTO.getSize(),
						Sort.by("mno").descending());
		
		Page<Member> list = null;

		log.error("serviceImpl isBan : " + isBan);
		
		if(isBan == null){
			if(id == null) {//검색 아이디 존재, 밴이 됨
				log.error("if id : " + id);
				list = memberRepository.getBanNonCheckList(pageable);
			}else {
				log.error("else id : " + id);
				list = memberRepository.getBanNonCheckList(id , pageable);
				log.error("list : " + list);
			}
		}else{
			if(id == null) {//검색 아이디 존재, 밴이 됨
				log.error("if id : " + id);
				list = memberRepository.getBanCheckList(isBan, pageable);
			}else {
				log.error("else id : " + id);
				list = memberRepository.getBanCheckList(isBan, id , pageable);
				log.error("list : " + list);
			}
		}

		List<MemberDTO> dtoList = list.getContent().stream()
				.map(member -> entityToDTO(member))
				.collect(Collectors.toList());
		
		long totalCount = list.getTotalElements();
		
		PageResponseDTO<MemberDTO> responseDTO = PageResponseDTO.<MemberDTO>withAll()
				.dtoList(dtoList)
				.pageRequestDTO(pageRequestDTO)
				.totalCount(totalCount)
				.build();
				
		return responseDTO;
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
	public MemberDTO findByMno(Long mno) {
		Member member = memberRepository.findById(mno)
				.orElseThrow(()-> new RuntimeException("멤버 정보를 찾을수 없음"));
		
		return new MemberDTO(member);
	}

	@Override
	public MemberDTO mypage(String email) {
		// TODO Auto-generated method stub
		return null;
	}
	
    }