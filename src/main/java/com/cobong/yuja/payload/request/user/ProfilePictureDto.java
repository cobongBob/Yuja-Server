package com.cobong.yuja.payload.request.user;

import com.cobong.yuja.model.ProfilePicture;

import lombok.Data;

@Data
public class ProfilePictureDto {
	private Long profilePicId;
	private Long userId;
	private String uploadPath;
	private String tempPath;
	private String fileName;
	private String origFilename;
	private boolean flag;

	public ProfilePicture toEntitiy() {
		return ProfilePicture.builder()
				.uploadPath(this.uploadPath)
				.fileName(this.fileName)
				.tempPath(this.tempPath)
				.origFilename(this.origFilename)
				.flag(this.flag)
				.build();
	}
}
