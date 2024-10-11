package com.example.portfolio.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorizeRequests -> authorizeRequests
						                                            .requestMatchers("/css/**", "/images/**", "/js/**", "/uploads/**").permitAll() // 정적 자원 허용
						                                            .requestMatchers("/", "/access/login", "/access/register", "/projects/{projectId}/tasklist", "/error", "/projects/download/**").permitAll() // 공개 페이지 허용
						                                            .requestMatchers("/projects/dashboard").authenticated() // 인증된 사용자만 접근 가능
						                                            .anyRequest().authenticated() // 나머지 요청은 인증 필요
				)
				.formLogin(formLogin -> formLogin
						                        .loginPage("/access/login") // 로그인 페이지의 경로
						                        .permitAll() // 모든 사용자에게 접근 허용
						                        .defaultSuccessUrl("/projects/dashboard", true)
						                        .failureUrl("/access/login?error=true")
				)
				.logout(logout -> logout
						                  .logoutUrl("/access/logout") // 로그아웃 경로 설정
						                  .logoutSuccessUrl("/access/login?logout=true")
						                  .invalidateHttpSession(true) // 세션 무효화
						                  .deleteCookies("JSESSIONID") // 쿠키 삭제
				)
				.exceptionHandling(exceptionHandling ->
						                   exceptionHandling
								                   .accessDeniedPage("/access/login") // 접근 거부 시 이동할 페이지
				)
				.csrf().disable();
		
		return http.build();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, accessDeniedException) -> {
			response.sendRedirect("/access/home");
		};
	}
}
