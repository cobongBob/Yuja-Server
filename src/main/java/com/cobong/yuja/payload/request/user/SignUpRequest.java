package com.cobong.yuja.payload.request.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
	
	@NotBlank
	@Size(min=4, max = 100)
	@Email
	private String username;

	@NotBlank
	@Size(min=8, max = 5, message = "제대로쓰셈")
	private String password;

	@NotBlank
	@Size(min=2, max = 30)
	private String realname;

	@NotBlank
	@Size(min=6, max = 7)
	private String bday;

	@NotBlank
	@Size(min=2, max = 20)
	private String nickname;

	private String profilePic;

	private String address;

	private String bsn;

	private String youtubeImg;
}
