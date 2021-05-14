package com.cobong.yuja.config;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.cobong.yuja.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomIpAuthenticationProvider implements AuthenticationProvider {
	
	private final UserService userService;
		
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		List<String> bannedList = userService.findAllBannedIp();
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String userIp = details.getRemoteAddress();
        
        if(bannedList.contains(userIp)) {
            throw new IllegalAccessError("Invalid IP Address");
        }
        return authentication;
    }
	
	@Override
	public boolean supports(Class<?> authentication) {
		
		return false;
		
	}

}
