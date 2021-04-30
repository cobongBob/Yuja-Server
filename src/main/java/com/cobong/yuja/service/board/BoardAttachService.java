package com.cobong.yuja.service.board;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

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
}
