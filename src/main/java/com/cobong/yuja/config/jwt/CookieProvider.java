package com.cobong.yuja.config.jwt;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public class CookieProvider {

    public Cookie createCookie(String cookieName, String value){
        Cookie token = new Cookie(cookieName,value);
        token.setHttpOnly(true); //http로만 쿠키사용가능(js로 접근 x)
        if(cookieName.equals(JwtTokenProvider.ACCESS_TOKEN_NAME)) {
        	token.setMaxAge((int)JwtTokenProvider.TOKEN_VALIDATION_SECOND/1000); //쿠키 만료
        } else {
        	token.setMaxAge((int)JwtTokenProvider.REFRESH_TOKEN_VALIDATION_SECOND/1000); //쿠키 만료
        }
        token.setPath("/"); // 요청이 온 path내 모든 경로에서 쿠키 사용가능
//        token.setSecure(true); https를 써야한다.
        return token;
    }
    
    public Cookie logOutCookie(String cookieName, String value) {
    	Cookie token = new Cookie(cookieName, value);
    	token.setHttpOnly(true);
    	token.setMaxAge(0);
    	token.setPath("/");
//    	token.setSecure(true);
    	return token;
    }

    public Cookie getCookie(HttpServletRequest req, String cookieName){
        final Cookie[] cookies = req.getCookies();
        if(cookies==null) return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }
    public Cookie createHitCookie(String cookieName, Long userId) {
    	Cookie token = new Cookie(cookieName,String.valueOf(userId));
        token.setPath("/");
        token.setMaxAge(66);
//      token.setSecure(true);
        return token;
    }
    
    public Cookie createVisitCookie(String cookieName, Long userId) {
    	Cookie visitCookie = new Cookie(cookieName, String.valueOf(userId));
    	visitCookie.setPath("/");
    	
    	LocalDateTime tomorrow = LocalDate.now().plusDays(1).atStartOfDay();
    	LocalDateTime now = LocalDateTime.now();
    	/***
    	 * 쿠키 생성시간과 익일 오는 오전 00:00와의 시간차를 맥스 에이지로 만들기.
    	 */
        visitCookie.setMaxAge(66);
    	return visitCookie;
    }
}
