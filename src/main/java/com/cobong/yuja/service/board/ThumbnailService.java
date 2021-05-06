package com.cobong.yuja.service.board;

import org.springframework.web.multipart.MultipartFile;

public interface ThumbnailService {

	Object saveFile(MultipartFile file);
	
}
