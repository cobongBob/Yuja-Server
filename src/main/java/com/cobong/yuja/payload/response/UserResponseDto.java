package com.cobong.yuja.payload.response;

import com.cobong.yuja.model.User;

import lombok.Getter;

@Getter
public class UserResponseDto {
	private Long id;
	private String username;
	private String password;
	private String nickname;
	private String realName;
	private String bday;
	private String profilePic;
	private String providedId;
	private String provider;
	private String address;
	private String phone;
	private String bsn;
	private String youtubeImg;
	private String userIp;

	public UserResponseDto entityToDto(User entity) {
		this.id = entity.getUserId();
		this.username = entity.getUsername();
		this.password = entity.getPassword();
		this.nickname = entity.getNickname();
		this.realName = entity.getRealName();
		this.bday = entity.getBday();
		this.profilePic = entity.getProfilePic();
		this.providedId = entity.getProvidedId();
		this.provider = entity.getProvider();
		this.address = entity.getAddress();
		this.phone = entity.getPhone();
		this.bsn = entity.getBsn();
		this.youtubeImg = entity.getYoutubeImg();
		this.userIp = entity.getUserIp();
		
		return this;
	}
	
	
}
