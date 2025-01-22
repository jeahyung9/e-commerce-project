package com.fullstack.springboot.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.util.JWTUtil;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
/*
 * Access Token Filter
 * 특정 경로(Path) 에 필터를 걸어서, 요청 전에 이 필터를 통과하도록 하는 설정
 * 자바계열은 대부분이 요청을 처리하기전-후에 이러한 필터를 chain 방식으로 연결해서 요청 전처리-후처리 작업을 함
 * 이 필터 계열은 스프링의 MVC 인터셉터, 시큐어의 filter 등을 이용해서 구현할 수 있음
 * 아래의 OncePerRequestFilter 클래스는 주로 모든 요청에 대해서 체크하는데 사용. 이중 특정 메서드를 통해 필터 체크를 하지 않는 경로를 지정하여 사용
 * 
 * 설정된 필터는 Config 에 추가해서 컨테이너가 필터에 추가할 수 있도록 함
 */
@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {
	
	//아래 메서드는 체크하지 않을 경로나 메서드(get/post) 등을 지정하기 위해 사용됨
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{

		//이미지나 member 에 관련된 path 는 필터를 적용하지 않도록 선언
		//또한 OPTIONS 에 관련한 메서드 요청도 필터가 처리하지 않음
//		if(request.getMethod().equals("OPTIONS")) {
//			return true;
//		}
//		
//		String path = request.getRequestURI();
//		log.error("check 대상 URI : " + path);
//		
//		if(path.startsWith("/api/member")) {
//			return true;
//		}
//		
//		if(path.startsWith("/api/products/view")) {//이미지 호출하는 path
//			return true;
//		}
		
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
			String email = (String)claims.get("email");
			String password = (String)claims.get("password");
			String name = (String)claims.get("name");
			
			
			MemberDTO dto = new MemberDTO(null, authHeader, accessToken, name, name, password, email, name, null, false, false, false, false, null, null);
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(dto, password, dto.getAuthorities());
			
			//SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
			//이것은 통과
		}catch (Exception e) {
			log.error("JWT Check Error");
			log.error("!!!!!!!" + e.getMessage());
			
			//에러 발생했으니 클라이언트에 메세지 전송
			Gson gson = new Gson();
			String msg = gson.toJson(Map.of("error","ERROR_TOKEN_ACCESS"));
			
			response.setContentType("application/JSON");
			PrintWriter out = response.getWriter();
			out.println(msg);
			out.close();
		}
		
		filterChain.doFilter(request, response);
	}

}