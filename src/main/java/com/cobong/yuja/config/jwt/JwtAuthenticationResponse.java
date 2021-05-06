package com.cobong.yuja.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {

	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";
	
	public JwtAuthenticationResponse(String accessToken,String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
