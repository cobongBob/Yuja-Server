package com.cobong.yuja.payload.response.user;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.cobong.yuja.model.User;

import lombok.Data;

@Data
public class UserResponseDto {
	private Long id;
	private String username;
	private String nickname;
	private String realName;
	private List<?> authorities;
	private String bday;
	private String profilePic;
	private Long profilePicId;
	private String providedId;
	private String provider;
	private String address;
	private String detailAddress;
	private String phone;
	private String bsn;
	private String youtubeUrl;
	private ZonedDateTime createDate;
	private ZonedDateTime updatedDate;
	private String youtubeConfirmImg;
	private boolean banned;
	private boolean deleted;
	
	public UserResponseDto entityToDto(User entity) {
		this.id = entity.getUserId();
		this.username = entity.getUsername();
		this.nickname = entity.getNickname();
		this.realName = entity.getRealName();
		this.authorities = entity.getAuthorities();
		this.bday = entity.getBday();
		this.providedId = entity.getProvidedId();
		this.provider = entity.getProvider();
		this.phone = entity.getPhone();
		this.bsn = entity.getBsn();
		this.youtubeUrl = entity.getYoutubeUrl();
		this.createDate = entity.getCreatedDate().atZone(ZoneId.of("Asia/Seoul"));
		this.updatedDate = entity.getUpdatedDate().atZone(ZoneId.of("Asia/Seoul"));
		this.banned = entity.isBanned();
		this.deleted = entity.isDeleted();
		return this;
	}
	
	
}
