package com.fullstack.springboot.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class GlobalExceptionHandler {
    
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", "error");
//        response.put("message", e.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
}
