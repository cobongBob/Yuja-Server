package com.cobong.yuja.service.board;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

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
	public BoardAttachDto getFile(Long id) {
		BoardAttach boardAttach = attachRepository.findById(id).get();
		
		BoardAttachDto attachDto = BoardAttachDto.builder()
				.board(boardAttach.getBoard())
				.uploadPath(boardAttach.getUploadPath())
				.fileName(boardAttach.getFileName())
				.origFilename(boardAttach.getOrigFilename())
				.build();
		return attachDto;
	}
}
