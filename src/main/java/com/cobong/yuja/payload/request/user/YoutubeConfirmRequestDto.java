package com.cobong.yuja.payload.request.user;

import lombok.Data;

@Data
public class YoutubeConfirmRequestDto {
	private Long youtubeConfirmId;
	private String bsn = "";
	private String youtubeUrl = "";
	private Long userId;
}
