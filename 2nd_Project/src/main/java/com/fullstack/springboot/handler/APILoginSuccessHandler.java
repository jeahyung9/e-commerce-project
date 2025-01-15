package com.fullstack.springboot.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fullstack.springboot.dto.member.MemberAuthDTO;
import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.util.JWTUtil;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.error("---------LogIn Success Handler---------");
		log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + authentication);
		log.error("---------End Of LogIn Success Handler---------");
		
		//인증된 사용자의 detail 을 리턴하는 메서드 호출
		MemberAuthDTO member = (MemberAuthDTO)authentication.getPrincipal();
		
//		Map<String, Object> claims = member.getClaims();
//		
//		String accessToken = JWTUtil.genToken(claims, 10);
//		String refreshToken = JWTUtil.genToken(claims, 1440);
//		
//		claims.put("accessToken", accessToken);
//		claims.put("refreshToken", refreshToken);
//		
//		//GSON 객체를 사용해서 Token 을 생성하고, 헤더 정보를 이용해서 응답
		Gson gson = new Gson();
//		String jsonStr = gson.toJson(claims);
		
		response.setContentType("application/json; char=UTF-8");
		PrintWriter out = response.getWriter();
//		out.print(jsonStr);
		out.close();
	}

}