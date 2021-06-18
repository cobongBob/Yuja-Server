package com.cobong.yuja.payload.response.user;

import java.util.List;

import com.cobong.yuja.model.User;

import lombok.Data;

@Data
public class UserForClientResponseDto {
	private Long id;
	private String username;
	private String nickname;
	private String realName;
	private List<?> authorities;
	private String profilePic;
	
	public UserForClientResponseDto entityToDto(User entity) {
		this.id = entity.getUserId();
		this.username = entity.getUsername();
		this.nickname = entity.getNickname();
		this.realName = entity.getRealName();
		this.authorities = entity.getAuthorities();
		return this;
	}
	
	
}
