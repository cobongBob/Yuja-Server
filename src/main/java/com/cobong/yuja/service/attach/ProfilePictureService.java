package com.cobong.yuja.service.attach;

import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.payload.response.attach.ProfilePictureDto;

public interface ProfilePictureService {
	ProfilePictureDto saveFile(MultipartFile file);

	void deleteUnflagged();

	String getProfileByName(String userNickname);
}
