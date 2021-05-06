package com.cobong.yuja.payload.request.board;

import com.cobong.yuja.model.ProfilePicture;
import com.cobong.yuja.model.Thumbnail;

import lombok.Data;

@Data
public class ThumbnailDto {
	private Long thumbnailId;
	private Long boardId;
	private String uploadPath;
	private String tempPath;
	private String fileName;
	private String origFilename;
	private boolean flag;

	public Thumbnail toEntitiy() {
		return Thumbnail.builder()
				.uploadPath(this.uploadPath)
				.fileName(this.fileName)
				.tempPath(this.tempPath)
				.origFilename(this.origFilename)
				.flag(this.flag)
				.build();
	}
}
