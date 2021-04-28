package com.cobong.yuja.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.payload.request.BoardUpdateRequestDto;
import com.cobong.yuja.payload.response.BoardResponseDto;
import com.cobong.yuja.repository.board.BoardRepository;
import com.google.common.base.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepository;
	
	@Override
	@Transactional
	public Board save(BoardSaveRequestDto dto) {
		Board board = boardRepository.save(dto.dtoToEntity());
		return board;
	}
	@Override
	@Transactional(readOnly = true)
	public BoardResponseDto findById(Long bno) {
		Board board = boardRepository.findById(bno).orElseThrow(() -> new IllegalAccessError("해당글 없음" + bno));
		BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
		return dto;
	}
	@Override
	@Transactional(readOnly = true)
	public List<BoardResponseDto> findAll() {
		List<BoardResponseDto> boards = new ArrayList<BoardResponseDto>();
		boardRepository.findAll().forEach(e -> boards.add(new BoardResponseDto().entityToDto(e)));
		return boards;
	}

	@Override
	@Transactional
	public String delete(Long bno) {
		 boardRepository.deleteById(bno);
		 return "success";
	}

	@Override
	@Transactional
	public BoardResponseDto modify(Long bno, BoardUpdateRequestDto boardUpdateRequestDto) {
		Board board = boardRepository.findById(bno)
				.orElseThrow(() -> new IllegalAccessError("해당글 없음" + bno));
		
		board.modify(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent(), 
				boardUpdateRequestDto.getThumbnail(),boardUpdateRequestDto.getPayType(),
				boardUpdateRequestDto.getPayAmount(), boardUpdateRequestDto.getCareer(),
				boardUpdateRequestDto.getTools(), boardUpdateRequestDto.getExpiredDate());
		BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
		return dto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BoardResponseDto> boardsInBoardType(Long boardCode){
		List<Board> curBoard = boardRepository.boardsInBoardType(boardCode);
		List<BoardResponseDto> curBoardResponseDto = new ArrayList<BoardResponseDto>();
		for(Board board: curBoard) {
			int likes = Long.valueOf(boardRepository.likedReceived(board.getBoardId())).intValue();
			int comments = Long.valueOf(boardRepository.commentsReceived(board.getBoardId())).intValue();
			BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
			dto.setLikesAndComments(likes, comments);
			curBoardResponseDto.add(dto);
		}
		return curBoardResponseDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BoardResponseDto> boardsUserWrote(Long userId) {
		List<Board> curBoard = boardRepository.boardsUserWrote(userId);
		List<BoardResponseDto> curBoardResponseDto = new ArrayList<BoardResponseDto>();
		for(Board board: curBoard) {
			int likes = Long.valueOf(boardRepository.likedReceived(board.getBoardId())).intValue();
			int comments = Long.valueOf(boardRepository.commentsReceived(board.getBoardId())).intValue();
			BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
			dto.setLikesAndComments(likes, comments);
			curBoardResponseDto.add(dto);
		}
		return curBoardResponseDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BoardResponseDto> boardsUserLiked(Long userId) {
		List<Board> curBoard = boardRepository.boardsUserLiked(userId);
		List<BoardResponseDto> curBoardResponseDto = new ArrayList<BoardResponseDto>();
		for(Board board: curBoard) {
			int likes = Long.valueOf(boardRepository.likedReceived(board.getBoardId())).intValue();
			int comments = Long.valueOf(boardRepository.commentsReceived(board.getBoardId())).intValue();
			BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
			dto.setLikesAndComments(likes, comments);
			curBoardResponseDto.add(dto);
		}
		return curBoardResponseDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BoardResponseDto> boardsUserCommented(Long userId) {
		List<Board> curBoard = boardRepository.boardsUserCommented(userId);
		List<BoardResponseDto> curBoardResponseDto = new ArrayList<BoardResponseDto>();
		for(Board board: curBoard) {
			int likes = Long.valueOf(boardRepository.likedReceived(board.getBoardId())).intValue();
			int comments = Long.valueOf(boardRepository.commentsReceived(board.getBoardId())).intValue();
			BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
			dto.setLikesAndComments(likes, comments);
			curBoardResponseDto.add(dto);
		}
		return curBoardResponseDto;
	}
}
