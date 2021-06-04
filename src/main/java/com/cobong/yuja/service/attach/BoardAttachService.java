package com.cobong.yuja.service.attach;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.model.BoardAttach;
import com.cobong.yuja.payload.request.attach.BoardAttachDto;
import com.cobong.yuja.repository.attach.AttachRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardAttachService {
	private final AttachRepository attachRepository;
	
	private final List<String> availableTypes = Arrays.asList(".jfif",".pjpeg",".pjp",".jpg",".jpeg",".png",".gif");
	
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
				
				if(!availableTypes.contains(fileType)) {
					/***
					 * 파일 형식이 ".jpg",".jpeg",".png",".gif" 중 하나가 아닐시 예외처리 필요
					 */
					throw new IllegalAccessError("파일형식은 jpg, jpeg, png, gif 중 하나여야 합니다!");
				};
				
				// 실행되는 위치의 'temp' 폴더에 파일이 저장
				String savePath = System.getProperty("user.dir") + File.separator+"files" + File.separator +"temp";
				// 파일이 저장되는 폴더가 없으면 폴더를 생성
				if (!new File(savePath).exists()) {
					try {
						new File(savePath).mkdirs(); //mkdirs는 폴더안에 폴더를 찾는데 그 상위폴더 조차 존재치 않으면 만들어준다.
					} catch (Exception e) {
						e.getStackTrace();
					}
				}
				savePath += File.separator + filename;
				
				file.transferTo(new File(savePath));
				
				String boardType = "";
				
				switch (Long.valueOf(boardCode).intValue()) {
				case 1:
					boardType += "Youtuber";
					break;
				case 2:
					boardType += "Editor";
					break;
				case 3:
					boardType += "Thumb";
					break;
				case 4:
					boardType += "Winwin";
					break;
				case 5:
					boardType += "Collabo";
					break;
				case 6:
					boardType += "CustomService";
					break;
				case 7:
					boardType += "Free";
					break;
				case 8:
					boardType += "Report";
					break;
				case 9:
					boardType += "Notice";
					break;
				case 10:
					boardType += "QnA";
					break;
				default:
					throw new IllegalAccessError("해당 게시판이 존재하지 않습니다.");
				}
				String uploadPath = System.getProperty("user.dir") + File.separator+"files" + File.separator +boardType;
				if (!new File(uploadPath).exists()) {
					try {
						new File(uploadPath).mkdirs();
					} catch (Exception e) {
						e.getStackTrace();
					}
				}
				uploadPath += File.separator + filename;

				attachDto.setOrigFilename(origFilename);
				attachDto.setUploadPath(uploadPath);
				attachDto.setFileName(filename);
				attachDto.setTempPath(savePath);
				
				BoardAttach boardAttach = attachRepository.save(attachDto.toEntitiy());
				attachDto.setAttachId(boardAttach.getId());
				boardAttachIds.add(attachDto);
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalAccessError("이미지 업로드 중 오류가 발생했습니다");
			}
		}
		return boardAttachIds;
	}
	
	@Transactional
	public void deleteUnflagged() {
		List<BoardAttach> attachesToDel = attachRepository.findAllByFlag();
		
		for(BoardAttach attach: attachesToDel) {
			attachRepository.delete(attach);
		}
	}
}
