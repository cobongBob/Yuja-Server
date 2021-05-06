package com.cobong.yuja.payload.request.user;

import javax.validation.constraints.NotBlank;

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

	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
