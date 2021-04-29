package com.cobong.yuja.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageApiController {

	@PostMapping("/api/imgUpload")
	public String postimgupload(@RequestParam("file") MultipartFile files) {
		return files.getOriginalFilename();
	}
}