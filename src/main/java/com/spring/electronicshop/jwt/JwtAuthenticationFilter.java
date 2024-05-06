package com.spring.electronicshop.jwt;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.electronicshop.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String REQUEST_ID_HEADER = "X-Request-ID";

	private final JWTUtils jwtUtils;

	private final UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String userEmail;
		if (authHeader == null || authHeader.isBlank()) {
			filterChain.doFilter(request, response);
			return;
		}
		jwtToken = authHeader.substring(7);
		userEmail = jwtUtils.extractUsername(jwtToken);
		UserDetails userDetails = userService.loadUserByUsername(userEmail);

		if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());
			token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			securityContext.setAuthentication(token);
			SecurityContextHolder.setContext(securityContext);
		}

		String requestId = UUID.randomUUID().toString();
		request.setAttribute("requestId", requestId);
		response.setHeader(REQUEST_ID_HEADER, requestId);

		filterChain.doFilter(request, response);

	}

}
