package com.cobong.yuja.service.attach;

import org.springframework.web.multipart.MultipartFile;

public interface ThumbnailService {

	Object saveFile(MultipartFile file);

	void deleteUnflagged();
	
}
