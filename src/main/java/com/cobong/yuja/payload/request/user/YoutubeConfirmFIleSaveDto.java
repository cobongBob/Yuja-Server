package com.cobong.yuja.payload.request.user;

import com.cobong.yuja.model.YoutubeConfirm;

import lombok.Data;

@Data
public class YoutubeConfirmFIleSaveDto {
	private Long youtubeConfirmId;
	private Long userId;
	private String uploadPath;
	private String tempPath;
	private String fileName;
	private String origFilename;
	private boolean flag;

	
	
	public YoutubeConfirm toEntitiy() {
		return YoutubeConfirm.builder()
				.uploadPath(this.uploadPath)
				.fileName(this.fileName)
				.tempPath(this.tempPath)
				.origFilename(this.origFilename)
				.flag(this.flag)
				.build();
	}
}
