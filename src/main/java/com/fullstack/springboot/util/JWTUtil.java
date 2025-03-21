package com.fullstack.springboot.util;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

//JWT 생성 및 validation 클래스 정의
//토큰 생성 및 검증만 할 예정이기 때문에 모두 static 으로 정의
public class JWTUtil {
	
	private static String key = "adfjhnasdfhasdufhdsaifhaudshfasdjfnadskfnadf";
	
	//암호화될 값을 가진 Map 과, Token 의 유지기간(expires) 를 분(int) 으로 할 메서드 정의
	public static String genToken(Map<String, Object> valueMap, int min) {
		System.out.println("토큰");
		//키 생성 로직
		//1. SecretKey 객체를 Keys 라는 클래스의 메서드를 통해 얻어냄
		SecretKey key = null;
		
		try {
			key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		String jwtToken = Jwts.builder().header().add(Map.of("typ","JWT")).and().claims(valueMap).issuedAt(Date.from(ZonedDateTime.now().toInstant())).signWith(key).compact();
		
		return jwtToken;
	}
	
	//Access 토큰 validate
	public static Map<String, Object> validToken(String token){
		Map<String, Object> claim = null;
		
		//서명한 값 get
		try {
			SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
			
			claim = Jwts.parser()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (WeakKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return claim;
	}

	public static void invalidateToken(String accessToken) {
		// TODO Auto-generated method stub
		
	}
}