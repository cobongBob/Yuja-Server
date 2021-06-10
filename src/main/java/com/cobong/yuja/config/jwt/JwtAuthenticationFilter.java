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

import com.cobong.yuja.config.auth.PrincipalDetails;
import com.cobong.yuja.model.RefreshToken;
import com.cobong.yuja.model.User;
import com.cobong.yuja.repository.refreshToken.RefreshTokenRepository;
import com.cobong.yuja.repository.user.UserRepository;

// Client에서 request로 엄어온 정보를 가지고 로그인에서 필터링
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private CookieProvider cookieProvider;
	
	@Autowired
    private RefreshTokenRepository refreshTokenRepository ;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Cookie jwtToken = cookieProvider.getCookie(request, JwtTokenProvider.ACCESS_TOKEN_NAME);
        String jwt = null;
        String refreshJwt = null;
        Long refreshUserId = null;
        
		try {
			// 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
			if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken.getValue()) && SecurityContextHolder.getContext().getAuthentication() == null) {
				jwt = jwtToken.getValue();
				Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
				
				//해당 유저가 벤 당했는지 체크하는 로직
				PrincipalDetails principalDetails = null;
		    	Long userId = 0L;
		    	if (authentication.getPrincipal() instanceof PrincipalDetails) {
		    		principalDetails = (PrincipalDetails) authentication.getPrincipal();
					userId = principalDetails.getUserId();
				}
		    	User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
		    	if(user.isBanned()) {
		    		Cookie accessToken = cookieProvider.logOutCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, null);
					Cookie refreshToken = cookieProvider.logOutCookie(JwtTokenProvider.REFRESH_TOKEN_NAME, null);
					response.addCookie(accessToken);
					response.addCookie(refreshToken);
					try {
						request.logout();
					} catch (ServletException e) {
						e.printStackTrace();
					}
		    	} else { // 밴당한게 아니라면
		    		SecurityContextHolder.getContext().setAuthentication(authentication);
		    	}
			} else { // 토큰이 정상이 아니라면 refresh토큰으로 검증시작
				Cookie oriRefreshToken = cookieProvider.getCookie(request, JwtTokenProvider.REFRESH_TOKEN_NAME);
				if (oriRefreshToken != null  && SecurityContextHolder.getContext().getAuthentication() == null) {
					refreshJwt = oriRefreshToken.getValue();
					if(refreshJwt != null){
		            	refreshUserId = jwtTokenProvider.getUserIdFromJWT(refreshJwt);
		            	RefreshToken refreshTokenFromDB = refreshTokenRepository.findByUserId(refreshUserId).orElseThrow(()->new IllegalArgumentException("존재하지않는 키"));
		            	//jwt안에 있는 userId로 DB에 있는 refresh토큰을 찾는다.
		            	
		            	//db에 있는 토큰과 방금 받은 토큰이 같다면 해당 정보를 Authentication 을 가져와서 SecurityContext 에 저장
		            	//그와 동시에 db에 updateDate를 갱신시켜주고 새 Access토큰을 발급해준다.
		                if(refreshTokenFromDB.getRefreshToken().equals(refreshJwt)){
		    				Authentication authentication = jwtTokenProvider.getAuthentication(refreshJwt);
		    				
		    				//해당 유저가 벤 당했는지 체크하는 로직
		    				PrincipalDetails principalDetails = null;
		    		    	Long userId = 0L;
		    		    	if (authentication.getPrincipal() instanceof PrincipalDetails) {
		    		    		principalDetails = (PrincipalDetails) authentication.getPrincipal();
		    					userId = principalDetails.getUserId();
		    				}
		    		    	User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
		    		    	if(user.isBanned()) {
		    		    		Cookie accessToken = cookieProvider.logOutCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, null);
		    					Cookie refreshToken = cookieProvider.logOutCookie(JwtTokenProvider.REFRESH_TOKEN_NAME, null);
		    					response.addCookie(accessToken);
		    					response.addCookie(refreshToken);
		    					try {
		    						request.logout();
		    					} catch (ServletException e) {
		    						e.printStackTrace();
		    					}
		    		    	} else { // 밴당한게 아니라면
		    		    		SecurityContextHolder.getContext().setAuthentication(authentication);  
		    		    		String newToken = jwtTokenProvider.generateToken(authentication);
		    		    		refreshTokenFromDB.updateValue(refreshJwt);
		    		    		refreshTokenRepository.save(refreshTokenFromDB);
		    		    		Cookie newAccessToken = cookieProvider.createCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, newToken);
		    		    		response.addCookie(newAccessToken);
		    		    	}
		    				
		    			}
		            } 	
				}
			}
		} catch (Exception e) {
			logger.error("doFilterInternal에서 검증 실패" + e);
		}
		filterChain.doFilter(request, response); // 스프링의 나머지 FilterChain들을 수행
	}
		
}
