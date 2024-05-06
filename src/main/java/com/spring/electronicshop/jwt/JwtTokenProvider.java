//package com.example.springmvc.jwt;
//
//import java.util.Date;
//
//import org.apache.juli.logging.Log;
//import org.springframework.stereotype.Component;
//
//import com.example.springmvc.config.CustomUserDetails;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.UnsupportedJwtException;
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@Slf4j
//public class JwtTokenProvider {
//	// Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
//	private final String JWT_SECRET = "huanba";
//
//	// Thời gian có hiệu lực của chuỗi jwt
//	private final long JWT_EXPIRATION = 604800000L;
//
//	public String generateToken(CustomUserDetails userDetails) {
//		Date now = new Date();
//		Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
//
//		return Jwts.builder().setSubject(Long.toString(userDetails.getUser().getId())).setExpiration(expiryDate)
//				.signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
//	}
//
//	public Long getUserIdFromJWT(String token) {
//		Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
//
//		return Long.parseLong(claims.getSubject());
//	}
//
//	public boolean validateToken(String authToken) {
//		try {
//			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
//			return true;
//		} catch (MalformedJwtException ex) {
//			System.out.println("Invalid JWT token");
//		} catch (ExpiredJwtException ex) {
//			System.out.println("Expired JWT token");
//		} catch (UnsupportedJwtException ex) {
//			System.out.println("Unsupported JWT token");
//		} catch (IllegalArgumentException ex) {
//			System.out.println("JWT claims string is empty.");
//		}
//		return false;
//	}
//}
//package com;


