package com.spring.electronicshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.spring.electronicshop.jwt.JwtAuthenticationFilter;
import com.spring.electronicshop.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserService userService;

	private final JwtAuthenticationFilter authenticationFilter;

	private final JwtAuthenticationEntryPoint authenticationEntryPoint;

	private final CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(c -> c.disable()).csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((request) -> {
			request.requestMatchers("/", "/home", "/register/**", "/login/**", "/home/", "/api/v1/login/**",
					"api/v1/categories/**", "/refresh-token", "/api/v1/products/**", "/admin/login", "/admin/register",
					"/files/**", "/admin/forgot-password", "/forgot-password/**", "/reset-password",
					"api/v1/reviews/**", "/api/v1/slider/**", "/swagger-ui/**", "/api-docs/**").permitAll()
					.requestMatchers("/api/v1/users/**", "/api/v1/admin/customer/**", "/api/v1/admin/discounts/**",
							"/api/v1/admin/oders/**", "/api/v1/admin/products/**", "/api/v1/admin/slider/**",
							"/api/v1/admin/reviews/**", "/api/v1/admin/categories/**", "/api/v1/admin/statistical/**")
					.hasAnyAuthority("ADMIN", "STAFF")
					.requestMatchers("/api/v1/admin/staff/**", "/api/v1/admin/roles/**").hasAnyAuthority("ADMIN")
					.anyRequest().authenticated();

		}).exceptionHandling(auth -> auth.authenticationEntryPoint(authenticationEntryPoint));
		http.exceptionHandling(auth -> auth.accessDeniedHandler(accessDeniedHandler));
		http.formLogin(request -> request.disable())
				.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class).logout((request) -> {
					request.disable();
				});
		return http.build();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
