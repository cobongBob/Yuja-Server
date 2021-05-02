package com.cobong.yuja.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import com.cobong.yuja.payload.request.board.BoardSaveRequestDto;
import com.cobong.yuja.service.board.BoardAttachService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AttachApiController {
	private final BoardAttachService attachService;

	@PostMapping("/api/board/img/upload")
	public ResponseEntity<?> write(@RequestParam("file") MultipartFile[] files, BoardSaveRequestDto dto) {
		List<BoardAttachDto> boardAttachIds = new ArrayList<>();
		for(MultipartFile file: files) {
			BoardAttachDto attachDto = new BoardAttachDto();
			try {
				String origFilename = file.getOriginalFilename();
				
				//시간을 파일이름을 만드는 방향으로 가자.
				String filename = UUID.randomUUID().toString()+ origFilename;
				// 실행되는 위치의 'temp' 폴더에 파일이 저장
				String savePath = System.getProperty("user.dir") + File.separator+"files" + File.separator +"temp";
				// 파일이 저장되는 폴더가 없으면 폴더를 생성
				if (!new File(savePath).exists()) {
					try {
						new File(savePath).mkdirs(); //mkdirs는 폴더안에 폴더를 찾는데 그 상위폴더 조차 존재치 않으면 만들어준다.
						System.out.println(savePath);
					} catch (Exception e) {
						e.getStackTrace();
					}
				}
				String uploadPath = savePath + File.separator + filename;
				file.transferTo(new File(uploadPath));
				
				attachDto.setOrigFilename(origFilename);
				attachDto.setUploadPath(uploadPath);
				attachDto.setFileName(filename);
				attachDto.setTempPath(savePath);
				boardAttachIds.add(attachService.saveFileEdited(attachDto));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(boardAttachIds, HttpStatus.OK);
	}
	
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