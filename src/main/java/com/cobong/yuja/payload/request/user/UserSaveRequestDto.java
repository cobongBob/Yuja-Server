package com.cobong.yuja.payload.request.user;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cobong.yuja.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequestDto {
	
	@NotBlank
	@Size(min=4, max = 100)
	@Email
	private String username;
	
	@NotBlank
	@Size(min=4, max = 20)
	private String password;
	
	@NotBlank
	@Size(min=2, max = 20)
	private String nickname;
	
	@NotBlank
	@Size(min=2, max = 30)
	private String realName;
	
	@NotBlank
	@Size(min=6, max = 7)
	private String bday;
	private String providedId ="";
	private String provider ="";
	private String address ="";
	private String phone ="";
	private String bsn ="";
	private String youtubeImg ="";
	private String userIp;
	private boolean isMarketingChecked;
	private Long profilePicId = 0L;
	
	public void setIsMarketingChecked(boolean isMarketingChecked) {
		this.isMarketingChecked = isMarketingChecked;
	}
	
	public boolean getIsMarketingChecked() {
		return this.isMarketingChecked;
	}
	
	public User dtoToEntity() {
		return User.builder()
				.username(this.username)
				.password(this.password)
				.nickname(this.nickname)
				.realName(this.realName)
				.bday(this.bday)
				.providedId(this.providedId)
				.provider(this.provider)
				.address(this.address)
				.phone(this.phone)
				.bsn(this.bsn)
				.youtubeImg(this.youtubeImg)
				.userIp(this.userIp)
				.isMarketingChecked(this.isMarketingChecked)
				.build();
	}
}
