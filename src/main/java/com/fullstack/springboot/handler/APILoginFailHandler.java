package com.fullstack.springboot.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class APILoginFailHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		log.error("로긴 실패 : ..." + exception);
		
		Gson gson = new Gson();
		
		String jsonStr = gson.toJson(Map.of("error", "ERROR_LOGIN"));
		response.setContentType("application/json");
		PrintWriter out=response.getWriter();
		out.print(jsonStr);
		out.close();
	}

}