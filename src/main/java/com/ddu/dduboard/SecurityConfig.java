package com.ddu.dduboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 스프링 부트의 환경설정 파일로 명시하는 annotation
@EnableWebSecurity // 모든 요청 url이 스프링 시큐리티의 제어를 받도록 만드는 annotation
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// 모든 요청에 대해 인증 없이 접근 허용
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
					.requestMatchers("/**").permitAll())
			;
		return http.build();
	}
	
	@Bean // 비밀번호 암호화
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
