package com.cobong.yuja.config.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.cobong.yuja.config.auth.PrincipalDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

// 로그인 한 후 JWT를 생성하고, JWT 유효성을 검사파트
@Component
public class JwtTokenProvider {
	public final static long TOKEN_VALIDATION_SECOND = 1000L * 20;
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 24;
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	
	final static public String ACCESS_TOKEN_NAME = "accessToken";
    final static public String REFRESH_TOKEN_NAME = "refreshToken";
    
	// apc.yml에 설정함
	@Value("${app.jwtSecret}")
	private String jwtSecret;
	
	
	public String generateToken(Authentication authentication) {
        return doGenerateToken(authentication, TOKEN_VALIDATION_SECOND);
    }

    public String generateRefreshToken(Authentication authentication) {
        return doGenerateToken(authentication, REFRESH_TOKEN_VALIDATION_SECOND);
    }
	// JWT 생성 파트
	public String doGenerateToken(Authentication authentication, long expireTime) {

		// getPrincipal PrincipalDetails담겨있는 정보를 가져옴
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		Date now = new Date();
		Date expirydate = new Date(now.getTime() + expireTime);

		return Jwts.builder()
				.setSubject(Long.toString(principalDetails.getUserId())) // sub에서 UserId를 구분
				.setIssuedAt(new Date())
				.setExpiration(expirydate) // 위 세개 PAYLOAD 
				.signWith(SignatureAlgorithm.HS512, jwtSecret) // HEADER
				.compact(); // 만듬
	}

	// JWT로 부터 UserId 가져옴(filter에서 토큰 유효성 검사용 디코딩으로 filter에서 check 할거임)
	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts
				.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();

		return Long.parseLong(claims.getSubject());
	}

	// 유효성 검사 파트
	// 헤더에서온 비암호화된 암호 파싱
	public boolean validateToken(String authToken) {

		try {
			Jwts.parser()
			.setSigningKey(jwtSecret)
			.parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			logger.error("claims에서ㅏ오류로 검증이 실패");
		} catch (MalformedJwtException ex) {
			logger.error("구조적인 문제가 있는 JWToken");
		} catch (ExpiredJwtException ex) {
			logger.error("유효기간 지난 JWToken");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWToken");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}
	public Claims extractAllClaims(String token) throws ExpiredJwtException {
		return Jwts
				.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();
	}
	
	 public Boolean isTokenExpired(String token) {
	        Date expiration = extractAllClaims(token).getExpiration();
	        return expiration.before(new Date());
	    }
}
