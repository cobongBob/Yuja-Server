package com.cobong.yuja.payload.request.user;

import com.cobong.yuja.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequestDto {
	private String username;
	private String password;
	private String nickname;
	private String realName;
	private String bday;
	private String providedId ="";
	private String provider ="";
	private String address ="";
	private String phone ="";
	private String bsn ="";
	private String youtubeImg ="";
	private String userIp;
	private boolean isMarketingChecked;
	private Long profilePicId;
	
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
