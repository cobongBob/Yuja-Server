package com.cobong.yuja.payload.request.user;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
