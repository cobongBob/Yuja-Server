package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.service.attach.BoardAttachService;
import com.cobong.yuja.service.attach.ThumbnailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AttachApiController {
	private final BoardAttachService attachService;
	private final ThumbnailService thumbnailService;

	@PostMapping("/api/{boardCode}/board/img/upload") //done
	public ResponseEntity<?> write(@RequestParam("file") MultipartFile[] files, @PathVariable Long boardCode) {
		return new ResponseEntity<>(attachService.saveFile(files, boardCode), HttpStatus.OK);
	}
	
	/***
	 * 썸네일러 썸네일(대표 이미지) 지정시 받는 컨트롤러
	 */
	@PostMapping("api/{boardCode}/thumbnail/upload") //done
	public ResponseEntity<?> uploadThumbnail(@RequestParam("file") MultipartFile file){
		return new ResponseEntity<>(thumbnailService.saveFile(file), HttpStatus.OK);
	}
}