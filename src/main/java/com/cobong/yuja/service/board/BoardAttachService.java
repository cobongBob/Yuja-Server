package com.cobong.yuja.service.board;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.model.BoardAttach;
import com.cobong.yuja.payload.request.board.BoardAttachDto;
import com.cobong.yuja.repository.attach.AttachRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardAttachService {
	private final AttachRepository attachRepository;
	
	@Transactional
	public List<BoardAttachDto> saveFile(MultipartFile[] files, Long boardCode) {
		List<BoardAttachDto> boardAttachIds = new ArrayList<>();
		for(MultipartFile file: files) {
			BoardAttachDto attachDto = new BoardAttachDto();
			try {
				String origFilename = file.getOriginalFilename();
				String fileType = origFilename.substring(origFilename.lastIndexOf("."));
				String dateNow = new SimpleDateFormat("YYYYMMddHHmmssSSSSSS").format(new Date());
				String filename = dateNow + fileType;
				
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
				savePath += File.separator + filename;
				
				file.transferTo(new File(savePath));
				
				String boardType = "";
				
				switch (Long.valueOf(boardCode).intValue()) {
				case 1:
					boardType += "YoutuberBoard";
					break;
				case 2:
					boardType += "EditorBoard";
					break;
				case 3:
					boardType += "ThumbBoard";
					break;
				}
				String uploadPath = System.getProperty("user.dir") + File.separator+"files" + File.separator +boardType + File.separator + filename;

				attachDto.setOrigFilename(origFilename);
				attachDto.setUploadPath(uploadPath);
				attachDto.setFileName(filename);
				attachDto.setTempPath(savePath);
				
				BoardAttach boardAttach = attachRepository.save(attachDto.toEntitiy());
				attachDto.setAttachId(boardAttach.getId());
				boardAttachIds.add(attachDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return boardAttachIds;
	}
	
	@Transactional
	public BoardAttachDto saveFileEdited(BoardAttachDto attachDto) {
		BoardAttach attachEntity = attachRepository.save(attachDto.toEntitiy());
		return BoardAttachDto.builder()
				.attachId(attachEntity.getId())
				.uploadPath(attachEntity.getUploadPath())
				.tempPath(attachEntity.getTempPath())
				.fileName(attachEntity.getFileName())
				.origFilename(attachEntity.getOrigFilename())
				.flag(attachEntity.isFlag()).build();
	}
	@Transactional(readOnly = true)
	public BoardAttachDto findById(Long attachId) {
		BoardAttach attachEntity = attachRepository.findById(attachId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 파일"));
		return BoardAttachDto.builder()
				.attachId(attachEntity.getId())
				.uploadPath(attachEntity.getUploadPath())
				.tempPath(attachEntity.getTempPath())
				.fileName(attachEntity.getFileName())
				.origFilename(attachEntity.getOrigFilename())
				.flag(attachEntity.isFlag()).build();
	}

	@Transactional
	public Object saveProfile(MultipartFile[] files, Long boardCode) {
		List<BoardAttachDto> boardAttachIds = new ArrayList<>();
		for(MultipartFile file: files) {
			BoardAttachDto attachDto = new BoardAttachDto();
			try {
				String origFilename = file.getOriginalFilename();
				String fileType = origFilename.substring(origFilename.lastIndexOf("."));
				String dateNow = new SimpleDateFormat("YYYYMMddHHmmssSSSSSS").format(new Date());
				String filename = dateNow + fileType;
				
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
				savePath += File.separator + filename;
				
				file.transferTo(new File(savePath));
				
				String boardType = "";
				
				switch (Long.valueOf(boardCode).intValue()) {
				case 1:
					boardType += "YoutuberBoard";
					break;
				case 2:
					boardType += "EditorBoard";
					break;
				case 3:
					boardType += "ThumbBoard";
					break;
				}
				String uploadPath = System.getProperty("user.dir") + File.separator+"files" + File.separator +boardType + File.separator + filename;

				attachDto.setOrigFilename(origFilename);
				attachDto.setUploadPath(uploadPath);
				attachDto.setFileName(filename);
				attachDto.setTempPath(savePath);
				
				BoardAttach boardAttach = attachRepository.save(attachDto.toEntitiy());
				attachDto.setAttachId(boardAttach.getId());
				boardAttachIds.add(attachDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return boardAttachIds;
	}
}
