package com.fullstack.springboot.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fullstack.springboot.filter.JWTFilter;
import com.fullstack.springboot.handler.CustomAccDeniedHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(t -> {
			t.configurationSource(corsConfigurationSource());
		});
		
		http.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.csrf(c -> c.disable());
//		http.formLogin(c -> {c.loginPage("/mall/login");
//			c.successHandler(new APILoginSuccessHandler());
//			c.failureHandler(new APILoginFailHandler());
//		});
		
		http.addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class);
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
		corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		corsConfiguration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		
		return source;
	}
	
}