package com.cobong.yuja.config.oauth;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class KakaoUser {
	private Map<String, Object> attribute;
	
	public KakaoUser(Map<String, Object> attribute) {
		this.attribute = attribute;
	}
	
	private String provider = "kakao";
	private String providerId;
	private Boolean flag;
	private String password;
	private String email;
}

