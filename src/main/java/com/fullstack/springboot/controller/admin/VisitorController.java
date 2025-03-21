package com.fullstack.springboot.controller.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/admin/visitor")
@Log4j2
public class VisitorController {
	
	private final ConcurrentHashMap<String, Boolean> visitorSet = new ConcurrentHashMap<>(); // Thread safe set (동시성을 위함)
	
	private final ConcurrentHashMap<String, Boolean> visitorHourSet = new ConcurrentHashMap<>();
	
	
	@PostMapping("/log")
	public void logVisitor(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			String ipAddress = getUserIp(request); // ip 주소 get
			
			//일일 방문자 카운트
			boolean newVisitor = checkAndSetCookie(request, response, "VISITED_IP", ipAddress, 60 * 60 * 24); //방문자 체크 및 신규 방문자 쿠키 세팅
			if(newVisitor) {//중복 방문자 아닐시 카운트 증가
				visitorSet.put(ipAddress, true);// 방문기록 저장
				log.warn("신규 일일 방문자 등록 : " + ipAddress);
			}else {
				log.warn("중복 일일 방문자 : " + ipAddress);
			}
			
			//시간대별 방문자 카운트
			boolean newVisitorHour = checkAndSetCookie(request, response, "VISITED_IP_HOUR", ipAddress, 60 * 60 * 1); 
			if(newVisitorHour) {//중복 방문자 아닐시 카운트 증가
				visitorHourSet.put(ipAddress, true);// 방문기록 저장
				log.warn("신규 시간대 방문자 등록 : " + ipAddress);
			}else {
				log.warn("중복 시간대 방문자 : " + ipAddress);
			}
		}catch(Exception e){
			log.error("방문자 기록 실패");
		}
	}
	
	@GetMapping("/count")
	public ResponseEntity<Map<String, Integer>> getVisitCount() {
		Map<String, Integer> response = new HashMap<>();
		response.put("counter", visitorSet.size());
		log.error("일일 방문자 수 : " + visitorSet.size());
		return ResponseEntity.ok(response); // 방문자 수 체크(유일성 검사를 통과한)
	}
	
	@GetMapping("/count/hour")
	public ResponseEntity<Map<String, Integer>> getVisitCountOfHour() {
		Map<String, Integer> response = new HashMap<>();
		response.put("counter", visitorHourSet.size());
		log.error("시간별 방문자 수 : " + visitorHourSet.size());
		return ResponseEntity.ok(response); //시간 별 방문자 체크
	}
	
	private String getUserIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For"); // 프록시 환경
		if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	private boolean checkAndSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String ipAdress, int maxAge) {
		
		if(hasCookie(request, cookieName, ipAdress)) {
			return false; // 중복 방문자 일 경우 false 반환
		}
		
		setCookie(response, cookieName, ipAdress, maxAge);//신규 방문자 쿠키 세팅
		return true; // 새 방문자일 경우 true 반환
	}
	
	private boolean hasCookie(HttpServletRequest request, String cookieName, String cookieValue) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookieName.equals(cookie.getName()) && cookieValue.equals(cookie.getValue())) {
					return true; // 쿠키가 있음
				}
			}
		}
		return false;//쿠키가 없음
	}
	
	private void setCookie(HttpServletResponse response, String cookieName, String value, int maxAge) { // 쿠키 세팅
		Cookie cookie = new Cookie(cookieName, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge); //24시간
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		response.setHeader("Set-Cookie", cookieName + "=" + value + "; Path=/; Max-Age=" + maxAge + "; HttpOnly; Secure; SameSite=None");
		log.error("쿠키 설정 완료" + cookie);
	}
}
