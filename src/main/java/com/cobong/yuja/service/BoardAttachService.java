package com.cobong.yuja.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cobong.yuja.model.BoardAttach;
import com.cobong.yuja.payload.request.AttachDto;
import com.cobong.yuja.repository.attach.AttachRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardAttachService {
	private final AttachRepository attachRepository;
	
	
	@Transactional
	public Long saveFile(AttachDto attachDto) {
		return attachRepository.save(attachDto.toEntitiy()).getId();
	}
	
	@Transactional
	public AttachDto getFile(Long id) {
		BoardAttach boardAttach = attachRepository.findById(id).get();
		
		AttachDto attachDto = AttachDto.builder()
				.board(boardAttach.getBoard())
				.uploadPath(boardAttach.getUploadPath())
				.fileName(boardAttach.getFileName())
				.origFilename(boardAttach.getOrigFilename())
				.build();
		return attachDto;
	}
	


}
