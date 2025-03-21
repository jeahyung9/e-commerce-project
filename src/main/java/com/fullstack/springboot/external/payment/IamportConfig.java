package com.fullstack.springboot.external.payment;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

@Configuration
@Getter
public class IamportConfig {
	
	//application.properties 의 값을 주입
    @Value("${iamport.apiKey}")
    private String apiKey;

    @Value("${iamport.apiSecret}")
    private String apiSecret;

    
    //RestTemplate -> HTTP 요청,응답 
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setConnectTimeout(5000);
//        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }
    
    
    
}