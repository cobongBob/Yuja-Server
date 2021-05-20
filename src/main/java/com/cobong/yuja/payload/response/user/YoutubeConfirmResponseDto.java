package com.cobong.yuja.payload.response.user;


import com.cobong.yuja.model.User;
import com.cobong.yuja.model.YoutubeConfirm;

import lombok.Data;

@Data
public class YoutubeConfirmResponseDto {
	private Long youtubeConfirmId;
	private String youtubeConfirmImg;
	private User user;
	
	public YoutubeConfirmResponseDto entityToDto(YoutubeConfirm entity) {
		this.youtubeConfirmId = entity.getYoutubeConfirmId();
		this.youtubeConfirmImg = entity.getFileName();
		this.user = entity.getUser();
		return this;
	}
}
