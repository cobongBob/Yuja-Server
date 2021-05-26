package com.cobong.yuja.payload.request.attach;

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
	private String originalFileTemp;
	private String originalFileDest;

	public Thumbnail toEntitiy() {
		return Thumbnail.builder()
				.uploadPath(this.uploadPath)
				.fileName(this.fileName)
				.tempPath(this.tempPath)
				.origFilename(this.origFilename)
				.flag(this.flag)
				.originalFileTemp(this.originalFileTemp)
				.originalFileDest(this.originalFileDest)
				.build();
	}
}
