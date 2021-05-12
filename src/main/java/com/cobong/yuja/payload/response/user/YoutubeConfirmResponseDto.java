package com.cobong.yuja.payload.response.user;

import com.cobong.yuja.model.YoutubeConfirm;

import lombok.Data;

@Data
public class YoutubeConfirmResponseDto {
	private Long youtubeConfirmId;
	private String youtubeConfirmImg;
	
	public YoutubeConfirmResponseDto entityToDto(YoutubeConfirm entity) {
		this.youtubeConfirmId = entity.getYoutubeConfirmId();
		this.youtubeConfirmImg = entity.getUploadPath();
		return this;
	}
}
