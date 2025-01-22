package com.fullstack.springboot.config;


import java.util.Arrays;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fullstack.springboot.filter.JWTCheckFilter;
import com.fullstack.springboot.handler.APILoginFailHandler;
import com.fullstack.springboot.handler.APILoginSuccessHandler;
import com.fullstack.springboot.handler.CustomAccDeniedHandler;
import com.fullstack.springboot.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.warn("Secure Config");
		
		http.cors(t->{
			t.configurationSource(corsConfigurationSource());
		});
		//세션 사용 안한다는 선언
		http.sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.csrf(c->c.disable());
		
		http.headers().httpStrictTransportSecurity().disable(); 
		
		http.formLogin(c -> {c.loginProcessingUrl("/api/member/login")
                .successHandler(new APILoginSuccessHandler())
                .failureHandler(new APILoginFailHandler());
		});
		//사용자 인증을 처리하는 필터 전에 토큰이 필요한 경로에 대한 필터를 수행하는 필터를 추가
//		http.addFilter(new JWTCheckFilter());
		http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling(expConfig -> {
			expConfig.accessDeniedHandler(new CustomAccDeniedHandler());
		});
		
		return http.build();
	}
	
	@Bean 
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		
		corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
		corsConfiguration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control", "Content-Type"));
		corsConfiguration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		
		return source;
	}
}