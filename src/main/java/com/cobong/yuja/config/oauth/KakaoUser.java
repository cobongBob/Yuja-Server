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
public class KakaoUser implements OAuthUserInfo {
	private Map<String, Object> attribute;
	
	public KakaoUser(Map<String, Object> attribute) {
		this.attribute = attribute;
	}
	
	@Override
	public String getProviderId() {
		return (String) attribute.get("id");
	}
	
	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		return (String) attribute.get("email");
	}
	
	@Override
	public String getName() {
		return (String) attribute.get("name");
	}
	
	private Boolean flag;
	private String password;
	private String kakaoName;
}

