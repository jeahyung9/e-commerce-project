package com.fullstack.springboot.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fullstack.springboot.dto.member.MemberAuthDTO;
import com.fullstack.springboot.util.JWTUtil;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

	//아래 메서드는 체크하지 않을 경로나 메서드(get/post) 등을 지정하기 위해 사용됨
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		
		String path = request.getRequestURI();
		log.error("check 대상 URI : " + path);
		
		if(path.startsWith("/api/member/login")) {
			return true;
		}

		if(path.startsWith("/api/member/join")) {
			return true;
		}

		if(path.startsWith("/api/member/check-id")) {
			return true;
		}

		if(path.startsWith("/api/member/refresh")) {
			return true;
		}
		
		return true;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.error("check 되지 않을 필터");
		//여기서는 Token 에 대한 validation 처리를 해서 인증 여부 판독
		try {
			String authHeader = request.getHeader("Authorization");
			
			String accessToken = authHeader.substring(7); 
			// 이렇게 하는 이유는 토큰이 서버로 올 때, Bearer 라는 prefix 로 같이 오기 때문
			//실제 필요한 정보는 Bearer 를 제외한 토큰값이라 위처럼 분리해서 값 만 추출
			
			Map<String, Object> claims = JWTUtil.validToken(accessToken);
			log.error("JWT claims : " + claims);
			
			//요청에 따른 권한을 처리하는 로직 수행
			String m_email = (String)claims.get("email");
			String m_pw = (String)claims.get("password");
			Set<String> roleSet = new HashSet((ArrayList)claims.get("roleSet"));
			
			MemberAuthDTO dto = new MemberAuthDTO(m_email, m_pw, roleSet);
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(dto, m_pw, dto.getAuthorities());
			
			filterChain.doFilter(request, response);//이건 통과..
		}catch (Exception e) {
			log.error("JWT 체크 에러 발생함...");
			log.error("12345123451235" + e.getMessage());
			
			// 예외가 발생했으니 클라이언트에게 메세지 전송
			Gson gson = new Gson();
			String msg = gson.toJson(Map.of("error","ERROR_TOKEN_ACCESS"));
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.println(msg);
			out.close();
		}
		
	}
	
}
