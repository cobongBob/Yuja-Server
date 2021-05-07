package com.cobong.yuja.config.jwt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public class CookieProvider {

    public Cookie createCookie(String cookieName, String value){
        Cookie token = new Cookie(cookieName,value);
        token.setHttpOnly(true); //http로만 쿠키사용가능(js로 접근 x)
        token.setMaxAge((int)JwtTokenProvider.TOKEN_VALIDATION_SECOND); //쿠키 만료
        token.setPath("/"); // 요청이 온 path내 모든 경로에서 쿠키 사용가능
//        token.setSecure(true); https를 써야한다.
        return token;
       
    }

    public Cookie getCookie(HttpServletRequest req, String cookieName){
        final Cookie[] cookies = req.getCookies();
        System.out.println(cookies);
        if(cookies==null) return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }

}
