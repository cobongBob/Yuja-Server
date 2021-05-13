package com.cobong.yuja.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomIpAuthenticationProvider implements AuthenticationProvider {
	
	public CustomIpAuthenticationProvider() {

	
	}
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String userIp = details.getRemoteAddress();
        return null;
//        if(bannedList.contains(userIp)) {
//            throw new BadCredentialsException("Invalid IP Address");
//        }
    }
	
	@Override
	public boolean supports(Class<?> authentication) {
		
		return false;
		
	}

}
