package com.cobong.yuja.service.board;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.board.BoardUpdateRequestDto;
import com.cobong.yuja.payload.request.board.BoardSaveRequestDto;
import com.cobong.yuja.payload.response.board.BoardResponseDto;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	
	@Override
	@Transactional
	public Board save(BoardSaveRequestDto dto) {
		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalAccessError("해당유저 없음 "+dto.getUserId()));
		Board board = new Board().createBoard(null, user, dto.getTitle(), dto.getContent(), dto.getThumbnail(), dto.getExpiredDate(),
				dto.getPayType(), dto.getPayAmount(), dto.getCareer(), dto.getTools());
		
		/***
		 * BoarCode 넣어야함!
		 */
		Board board2 = boardRepository.save(board);
		return board2;
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
			//dto.setLikesAndComments(likes, comments);
			/*
			 * 유저 아이디 값을 받도록 바꾸어야 한다.
			 * */
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
			boolean likedOrNot = boardRepository.likedOrNot(board.getBoardId(), userId);
			int likes = Long.valueOf(boardRepository.likedReceived(board.getBoardId())).intValue();
			int comments = Long.valueOf(boardRepository.commentsReceived(board.getBoardId())).intValue();
			BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
			dto.setLikesAndComments(likes, comments, likedOrNot);
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
			boolean likedOrNot = boardRepository.likedOrNot(board.getBoardId(), userId);
			int likes = Long.valueOf(boardRepository.likedReceived(board.getBoardId())).intValue();
			int comments = Long.valueOf(boardRepository.commentsReceived(board.getBoardId())).intValue();
			BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
			dto.setLikesAndComments(likes, comments, likedOrNot);
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
			boolean likedOrNot = boardRepository.likedOrNot(board.getBoardId(), userId);
			int likes = Long.valueOf(boardRepository.likedReceived(board.getBoardId())).intValue();
			int comments = Long.valueOf(boardRepository.commentsReceived(board.getBoardId())).intValue();
			BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
			dto.setLikesAndComments(likes, comments, likedOrNot);
			curBoardResponseDto.add(dto);
		}
		return curBoardResponseDto;
	}
}
