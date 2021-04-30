package com.cobong.yuja.controller;

import java.io.File;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.payload.request.board.BoardAttachDto;
import com.cobong.yuja.payload.request.board.BoardSaveRequestDto;
import com.cobong.yuja.service.board.BoardAttachService;
import com.cobong.yuja.service.board.BoardService;
import com.cobong.yuja.util.MD5Generator;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AttachApiController {
	private final BoardAttachService attachService;
	private final BoardService boardService;

//	public AttachApiController(BoardService boardService, BoardAttachService attachService) {
//		this.boardService = boardService;
//		this.attachService = attachService;
//	}

	@PostMapping("/api/board/img/upload")
	public String write(@RequestParam("file") MultipartFile files, BoardSaveRequestDto dto) {
		try {
			String origFilename = files.getOriginalFilename();
			String fileType = origFilename.substring(origFilename.indexOf("."));
			System.out.println(fileType);
			String filename = new MD5Generator(origFilename).toString()+fileType;
			// 실행되는 위치의 'temp' 폴더에 파일이 저장
			String savePath = System.getProperty("user.dir") + "\\files\\temp";
			System.out.println(savePath);
			// 파일이 저장되는 폴더가 없으면 폴더를 생성
			if (!new File(savePath).exists()) {
				try {
					new File(savePath).mkdir();
					System.out.println(savePath);
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			
			String uploadPath = savePath + "\\" + filename;
			files.transferTo(new File(uploadPath));

			BoardAttachDto attachDto = new BoardAttachDto();
			attachDto.setOrigFilename(origFilename);
			attachDto.setUploadPath(uploadPath);
			attachDto.setFileName(filename);

			Long fileId = attachService.saveFile(attachDto);
			dto.setUserId(fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}
}