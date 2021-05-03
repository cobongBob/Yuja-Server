package com.cobong.yuja.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.payload.request.board.BoardAttachDto;
import com.cobong.yuja.service.board.BoardAttachService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AttachApiController {
	private final BoardAttachService attachService;

	//@PostMapping("/api/{boardCode}/board/img/upload")  @PathVariable Long boardCode ==> add it on parameter
	@PostMapping("/api/board/img/upload")
	public ResponseEntity<?> write(@RequestParam("file") MultipartFile[] files) {
		Long boardCode = 1L; //테스트용 보드 코드
		return new ResponseEntity<>(attachService.saveFile(files, boardCode), HttpStatus.OK);
	}
	/***
	 * 회원가입시 프로필 사진 업로드용 컨트롤러 필요
	 */
	
	//@PostMapping("/api/register/profilePicture/upload")
	@PostMapping("/api/register/profilePicture/upload")
	public ResponseEntity<?> profileUpload(@RequestParam("file") MultipartFile[] files) {
		Long boardCode = 1L;
		return new ResponseEntity<>(attachService.saveProfile(files, boardCode), HttpStatus.OK);
	}
	
	
	/***
	 * 썸네일러 썸네일 
	 */
	
	//파일 다운로드를 위한 로직인데.. 필요한가?
	@GetMapping("/api/board/img/download/{attachId}")
	public ResponseEntity<?> send(@PathVariable Long attachId) {
		BoardAttachDto attachDto = attachService.findById(attachId);
		Path path = Paths.get(attachDto.getUploadPath());
		Resource resource = null;
	    try {
			resource = new InputStreamResource(Files.newInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachDto.getOrigFilename()+"\"")
				.body(resource);
	}
}