package com.cobong.yuja.payload.response.user;


import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.cobong.yuja.model.YoutubeConfirm;

import lombok.Data;

@Data
public class YoutubeConfirmResponseDto {
	private Long youtubeConfirmId;
	private String youtubeConfirmImg;
	private UserResponseDto user;
	private ZonedDateTime createdDate;
	
	public YoutubeConfirmResponseDto entityToDto(YoutubeConfirm entity) {
		this.youtubeConfirmId = entity.getYoutubeConfirmId();
		this.youtubeConfirmImg = entity.getFileName();
		this.createdDate = entity.getCreatedDate().atZone(ZoneId.of("Asia/Seoul"));
		return this;
	}
}
