package com.fullstack.springboot.util;

public class CustomJWTException extends RuntimeException {
	
	public CustomJWTException(String msg) {
		super(msg);
	}
}