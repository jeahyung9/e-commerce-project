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
		//키 생성 로직
		//1. SecretKey 객체를 Keys 라는 클래스의 메서드를 통해 얻어냄
		SecretKey key = null;
		
		try {
			key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		//2. JWT 객체를 이용해서 헤더 정보, claims(=메인 정보),? , 보존 기간을 설정하고 생성된 Secret Key 로 서명을 해줌
		//이 서명을 유일성을 보장받기 때문에 절대 변조가 불가능
		String jwtStr = Jwts.builder().setHeader(Map.of("typ", "JWT"))
					.setClaims(valueMap)
					.setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
					.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
					.signWith(key)
					.compact();
		
		return jwtStr;
		
		/*
		 * JWT 토큰 : 크게 Access, Refresh 로 나뉘는데 토큰을 생성할 때 일반적으로 2개 모두 생성함
		 * 목적으로는 Access 토큰은 유효시간을 짧게(min 단위) 주는데 서버에 인증을 요청할 때마다 사용이 된다
		 * 이 때, 짧은 시간만을 가지게 되면 계속 서버에서 다시 생성해서 줘야하는 불편함이 있기 때문에 
		 * 유효시간이 긴 Refresh 토큰을 생성하서 Access 토큰의 유효시간을 연장하도록 설계
		 */
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
}