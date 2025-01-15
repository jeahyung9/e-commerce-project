package com.fullstack.springboot.controller.admin;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/visitor")
@Log4j2
public class VisitorController {
	
	private final ConcurrentHashMap<String, Boolean> visitorSet = new ConcurrentHashMap<>(); // Thread safe set (동시성을 위함)
	private final ConcurrentHashMap<String, Boolean> visitorHourSet = new ConcurrentHashMap<>();
	
	
	@PostMapping("/log")
	public void logVisitor(HttpServletRequest request, HttpServletResponse response) {
		String ipAddress = getUserIp(request); // ip 주소 get
		boolean newVisitor = checkAndSetCookie(request, response, ipAddress); // 쿠키 중복(동일 방문자 체크)
		boolean newVisitorHour = timeCheckAndSetCookie(request, response, ipAddress);
		
		//중복 방문자 아닐시 카운트 증가
		if(newVisitor) {
			visitorSet.put(ipAddress, true); // 방문기록 저장
		}
		
		if(newVisitorHour) {
			visitorHourSet.put(ipAddress, true); // 시간 별 방문기록 저장
		}
	}
	
	@GetMapping("/count")
	public int getVisitCount() {
		return visitorSet.size(); // 방문자 수 체크(유일성 검사를 통과한)
	}
	
	@GetMapping("/count/hour")
	public int getVisitCountOfHour() {
		return visitorHourSet.size(); //시간 별 방문자 체크
	}
	
	private String getUserIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For"); // 프록시 환경
		if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	private boolean checkAndSetCookie(HttpServletRequest request, HttpServletResponse response, String ipAddress) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if("VISITED_IP".equals(cookie.getName()) && ipAddress.equals(cookie.getValue())){
					return false; // 이미 방문한 경우 false 반환
				}
			}
		}
		
		//새 방문자 쿠키 설정
		Cookie cookie = new Cookie("VISITED_IP", ipAddress);
		cookie.setPath("/");
		cookie.setMaxAge(60*60*24); //24시간
		response.addCookie(cookie);
		
		return true; // 새 방문자일 경우 true 반환
	}
	
	private boolean timeCheckAndSetCookie(HttpServletRequest request, HttpServletResponse response, String ipAddress) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if("VISITED_IP_HOUR".equals(cookie.getName()) && ipAddress.equals(cookie.getValue())){
					return false; // 이미 방문한 경우 false 반환
				}
			}
		}
		
		//새 방문자 쿠키 설정
		Cookie cookie = new Cookie("VISITED_IP_HOUR", ipAddress);
		cookie.setPath("/");
		cookie.setMaxAge(60*60*1); 
		response.addCookie(cookie);
		
		return true; // 새 방문자일 경우 true 반환
	}
}
