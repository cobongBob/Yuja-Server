package com.cobong.yuja.config.oauth;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class GoogleUser implements OAuthUserInfo {
	private Map<String, Object> attribute;
	
	public GoogleUser(Map<String, Object> attribute) {
		this.attribute = attribute;
	}
	@Override
	public String getProviderId() {
		return (String) attribute.get("googleId");
	}
	@Override
	public String getProvider() {
		return "google";
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
}