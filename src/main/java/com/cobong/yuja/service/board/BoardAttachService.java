package com.cobong.yuja.service.board;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.BoardAttach;
import com.cobong.yuja.payload.request.board.BoardAttachDto;
import com.cobong.yuja.repository.attach.AttachRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardAttachService {
	private final AttachRepository attachRepository;
	
	@Transactional
	public Long saveFile(BoardAttachDto attachDto) {
		return attachRepository.save(attachDto.toEntitiy()).getId();
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
}
