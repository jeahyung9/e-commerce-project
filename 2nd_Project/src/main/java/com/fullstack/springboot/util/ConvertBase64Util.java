package com.fullstack.springboot.util;

import java.util.Base64;

public class ConvertBase64Util {
	
	public static String encoding(Long id) {
		return Base64.getUrlEncoder().encodeToString(id.toString().getBytes());
	}
	
	public static Long decoding(String encodeId) {
		String decodeStr = new String(Base64.getUrlDecoder().decode(encodeId));
		
		return Long.parseLong(decodeStr);
	}
}
