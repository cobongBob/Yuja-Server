package com.cobong.yuja.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cobong.yuja.model.RefreshToken;
import com.cobong.yuja.repository.RefreshTokenRepository;

// Client에서 request로 엄어온 정보를 가지고 로그인에서 필터링
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private CookieProvider cookieProvider;
	
	@Autowired
    private RefreshTokenRepository refreshTokenRepository ;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Cookie jwtToken = cookieProvider.getCookie(request, JwtTokenProvider.ACCESS_TOKEN_NAME);
        String jwt = null;
        String refreshJwt = null;
        Long refreshUserId = null;
        
		try {
			// 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
			if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken.getValue())) {
				jwt = jwtToken.getValue();
				Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else { // 토큰이 정상이 아니라면 refresh토큰으로 검증시작
				Cookie refreshToken = cookieProvider.getCookie(request, JwtTokenProvider.REFRESH_TOKEN_NAME);
				if (refreshToken != null) {
					refreshJwt = refreshToken.getValue();
					if(refreshJwt != null){
		            	refreshUserId = jwtTokenProvider.getUserIdFromJWT(refreshJwt);
		            	RefreshToken refreshTokenFromDB = refreshTokenRepository.findByUserId(refreshUserId).orElseThrow(()->new IllegalArgumentException("존재하지않는 키"));
		            	//jwt안에 있는 userId로 DB에 있는 refresh토큰을 찾는다.
		            	
		            	//db에 있는 토큰과 방금 받은 토큰이 같다면 해당 정보를 Authentication 을 가져와서 SecurityContext 에 저장
		            	//그와 동시에 db에 updateDate를 갱신시켜주고 새 Access토큰을 발급해준다.
		                if(refreshTokenFromDB.getRefreshToken().equals(refreshJwt)){
		    				Authentication authentication = jwtTokenProvider.getAuthentication(refreshJwt);
		    				SecurityContextHolder.getContext().setAuthentication(authentication);  
		    				String newToken = jwtTokenProvider.generateToken(authentication);
		    				refreshTokenFromDB.updateValue(refreshJwt);
		    		        refreshTokenRepository.save(refreshTokenFromDB);
		    				Cookie newAccessToken = cookieProvider.createCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, newToken);
		    				response.addCookie(newAccessToken);
		    			}
		            } else {
		            	logger.error("존재하지 않는 토큰");
		            }		
				}
			}
		} catch (Exception e) {
			logger.error("doFilterInternal에서 검증 실패" + e);
		}
		
		filterChain.doFilter(request, response); // 스프링의 나머지 FilterChain들을 수행
	}
		


//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		
//		try {
//					String jwt = getjwtFromRequest(request);
//			
//			// 헤더에서 가져온 디코딩된 토큰이 문자열이 아예없거나 
//			if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//				Long userId = tokenProvider.getUserIdFromJWT(jwt);
//				
//				PrincipalDetails principalDetails = principalDetailsService.loadUserById(userId);
//				
//				// 비번빼고 아이디 권한 UsernamePasswordAuthenticationToken에 저장
//				// AuthenticationManager에게 전달
//				UsernamePasswordAuthenticationToken authenticationToken =
//						new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
//				
//				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				
//				// SessionCreationPolicy.STATELESS
//				// 인증 정보가 일치함으로 context 에 인증정보를 저장하고 통과시키고  SecurityContextHolder에 set
//				SecurityContextHolder.getContext().setAuthentication(authenticationToken); 
//			}
//		} catch (Exception e) {
//			logger.error("doFilterInternal에서 검증 실패"+e);
//		}
//		filterChain.doFilter(request, response); // 스프링의 나머지 FilterChain들을 수행
//	}
//	
//	
//	// 클라 헤더에서 암호화 안된 토큰 가져옴(doFilterInternal에서 )
//	private String getjwtFromRequest(HttpServletRequest request) {
//		
//		// Authorization 헤더는 인증 토큰(JWT든, Bearer 토큰이든)을 서버로 보낼 때 사용하는 헤더
//		// API 요청같은 것을 할 때 토큰이 없으면 거절당하기 때문에 이 때, Authorization을 사용하면 됨
//		String bearToken = request.getHeader("Authorization");
//		
//		if(StringUtils.hasText(bearToken) && bearToken.startsWith("Bearer")) {
//			return bearToken.substring(7, bearToken.length()); // Bearer 제외 시키고 리턴
//		}
//		
//		return null;
//	}

}
