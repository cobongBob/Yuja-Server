package com.cobong.yuja.payload.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	private String username;

	private String password;
	
	private boolean rememberMe;
	   
	public boolean getRememberMe() {
	   return this.rememberMe;
	}
}
