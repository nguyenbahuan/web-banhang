package com.spring.electronicshop.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.electronicshop.jwt.JWTUtils;
import com.spring.electronicshop.jwt.JwtAuthenticationFilter;
import com.spring.electronicshop.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

	private final JWTUtils jwtUtils;
	private final UserService userService;

	@Bean
	FilterRegistrationBean<JwtAuthenticationFilter> requestIdFilterRegistrationBean() {
		FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtAuthenticationFilter(jwtUtils, userService));
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}
}
