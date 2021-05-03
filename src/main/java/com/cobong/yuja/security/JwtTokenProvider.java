//package com.cobong.yuja.security;
//
//import java.util.Date;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import com.cobong.yuja.config.auth.PrincipalDetails;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.SignatureException;
//import io.jsonwebtoken.UnsupportedJwtException;
//
//// 로그인 한 후 JWT를 생성하고, JWT 유효성을 검사파트
//@Component
//public class JwtTokenProvider {
//
//	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
//
//	// (yml에서 설정함)
//	@Value("${app.jwtSecret}")
//	private String jwtSecret;
//
//	@Value("${app.jwtExpirationInMs}")
//	private int jwtExpirationInMs;
//
//	// JWT 생성
//	public String generateToken(Authentication authentication) {
//
//		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//
//		Date now = new Date();
//		Date expirydate = new Date(now.getTime() + jwtExpirationInMs);
//
//		return Jwts.builder().setSubject(Long.toString(principalDetails.getUserid())) //
//				.setIssuedAt(new Date())
//				.setExpiration(expirydate)
//				.signWith(SignatureAlgorithm.HS512, jwtSecret) 
//				.compact(); 
//	}
//
//	// JWT로 부터 UserId 가져옴(filter에서 토큰 유효성 검사용)
//	public Long getUserIdFromJWT(String token) {
//		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
//
//		return Long.parseLong(claims.getSubject());
//	}
//
//	// 유효성 검사
//	public boolean validateToken(String authToken) {
//
//		try {
//			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//			return true;
//		} catch (SignatureException ex) {
//			logger.error("claims에서ㅏ오류로 검증이 실패");
//		} catch (MalformedJwtException ex) {
//			logger.error("구조적인 문제가 있는 JWToken");
//		} catch (ExpiredJwtException ex) {
//			logger.error("유효기간 지난 JWToken");
//		} catch (UnsupportedJwtException ex) {
//			logger.error("Unsupported JWToken");
//		} catch (IllegalArgumentException ex) {
//			logger.error("JWT claims string is empty.");
//		}
//		return false;
//	}
//}
