package com.cobong.yuja.service.user;

import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.payload.response.user.ProfilePictureDto;

public interface ProfilePictureService {
	ProfilePictureDto saveFile(MultipartFile file);

	void deleteUnflagged();

	String getProfileByName(String userNickname);
}
