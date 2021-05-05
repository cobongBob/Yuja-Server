package com.cobong.yuja.security;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class LoginRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
