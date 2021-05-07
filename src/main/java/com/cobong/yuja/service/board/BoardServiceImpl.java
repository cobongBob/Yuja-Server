package com.cobong.yuja.service.board;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardAttach;
import com.cobong.yuja.model.BoardType;
import com.cobong.yuja.model.Thumbnail;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.board.BoardSaveRequestDto;
import com.cobong.yuja.payload.request.board.BoardUpdateRequestDto;
import com.cobong.yuja.payload.response.board.BoardResponseDto;
import com.cobong.yuja.repository.BoardTypeRepository;
import com.cobong.yuja.repository.attach.AttachRepository;
import com.cobong.yuja.repository.attach.ThumbnailRepository;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.user.UserRepository;
import com.google.common.io.Files;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepository;
	private final BoardTypeRepository boardTypeRepository;
	private final UserRepository userRepository;
	private final AttachRepository attachRepository;
	private final ThumbnailRepository thumbnailRepository;
	
	@Override
	@Transactional
	public BoardResponseDto save(BoardSaveRequestDto dto) {
		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalAccessError("해당유저 없음 "+dto.getUserId()));
		BoardType boardType = boardTypeRepository.findById(dto.getBoardCode()).orElseThrow(() -> new IllegalAccessError("해당글 타입 없음" + dto.getBoardCode()));
		
		String toolsCombined = String.join(",", dto.getTools());
		
		Board board = new Board().createBoard(boardType, user, dto.getTitle(), dto.getContent(), dto.getExpiredDate(),
				dto.getPayType(), dto.getPayAmount(), dto.getCareer(), toolsCombined, dto.getWorker(), dto.getYWhen());
		Board board2 = boardRepository.save(board);
		//null일경우 처리 필요
		if(dto.getBoardAttachIds() != null) {
			for(Long i: dto.getBoardAttachIds()) {
				BoardAttach boardAttach = attachRepository.findById(i).orElseThrow(() -> new IllegalAccessError("해당 이미지 없음 "+i));
				if(!boardAttach.isFlag()) {
					File temp = new File(boardAttach.getTempPath());
					File dest = new File(boardAttach.getUploadPath());
					try {
						Files.move(temp, dest);
					} catch (IOException e) {
						e.printStackTrace();
					}
					boardAttach.completelySave();
				}
				boardAttach.addBoard(board2);
				attachRepository.save(boardAttach);
			}
		} 
		
		if(dto.getThumbnailId() != null && dto.getThumbnailId() != 0L) {
			Optional<Thumbnail> thumbnailtodel = thumbnailRepository.findById(dto.getThumbnailId());
			if(thumbnailtodel.isPresent()) {
				Thumbnail thumbnail = thumbnailtodel.get();
				if(!thumbnail.isFlag()) {
					File temp = new File(thumbnail.getTempPath());
					File dest = new File(thumbnail.getUploadPath());
					File origTemp = new File(thumbnail.getOriginalFileTemp());
					File origDest = new File(thumbnail.getOriginalFileDest());
					try {
						Files.move(temp, dest);
						Files.move(origTemp, origDest);
					} catch (IOException e) {
						e.printStackTrace();
					}
					thumbnail.completelySave();
				}
				thumbnail.addBoard(board2);
				thumbnailRepository.save(thumbnail);
			}
		}
		return new BoardResponseDto().entityToDto(board2);
	}
	@Override
	@Transactional(readOnly = true)
	public BoardResponseDto findById(Long bno) {
		Board board = boardRepository.findById(bno).orElseThrow(() -> new IllegalAccessError("해당글 없음" + bno));
		List<String> tools = new ArrayList<>();
		if(board.getTools() != null) {
			tools = Arrays.asList(board.getTools().split(","));
		}
		
		int likes = Long.valueOf(boardRepository.likedReceived(board.getBoardId())).intValue();
		int comments = Long.valueOf(boardRepository.commentsReceived(board.getBoardId())).intValue();
		
		List<String> boardAttachesToSend = new ArrayList<String>();
		List<BoardAttach> attaches = attachRepository.findAllByBoardId(bno);
		for(BoardAttach boardAttach: attaches) {
			boardAttachesToSend.add(boardAttach.getFileName());
		}
		
		Optional<Thumbnail> thumbnailtodel = thumbnailRepository.findByBoardBoardId(bno);
		String thumbnailOrig = "";
		if(thumbnailtodel.isPresent()) {
			Thumbnail thumbnail = thumbnailtodel.get();
			if(thumbnail.getOriginalFileDest() != null && thumbnail.getOriginalFileDest().length() != 0) {
				thumbnailOrig += thumbnail.getOriginalFileDest();
			}
		}
		/***
		 * findById일때는 썸네일 테이블에서 오리지널 파일을 보내준다
		 */
		
		BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
		dto.setLikesAndComments(likes, comments);
		dto.setAttaches(boardAttachesToSend);
		dto.setTools(tools);
		dto.setThumbnail(thumbnailOrig);
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
		List<BoardAttach> attaches = attachRepository.findAllByBoardId(bno);
		for(BoardAttach boardAttach: attaches) {
			File toDel = new File(boardAttach.getUploadPath());
			if(toDel.exists()) {
				toDel.delete();				
			} else {
				System.out.println("Such File does not exist!");
				/***
				 * 파일 존재하지 않을시 예외처리 필요
				 */
			}
		}
		
		
		Optional<Thumbnail> thumbnailtodel = thumbnailRepository.findByBoardBoardId(bno);
		if(thumbnailtodel.isPresent()) {
			Thumbnail thumbnail = thumbnailtodel.get();
			File thumbToDel = new File(thumbnail.getUploadPath());
			if(thumbToDel.exists()) {
				thumbToDel.delete();
			} else {
				/***
				 * 파일 존재하지 않을시 예외처리 필요
				 */
			}
			
			File origToDel = new File(thumbnail.getOriginalFileDest());
			if(origToDel.exists()) {
				origToDel.delete();
			} else {
				/***
				 * 파일 존재하지 않을시 예외처리 필요
				 */
			}			
		}
		
		boardRepository.deleteById(bno);
		return "success";
	}

	@Override
	@Transactional
	public BoardResponseDto modify(Long bno, BoardUpdateRequestDto boardUpdateRequestDto) {
		Board board = boardRepository.findById(bno)
				.orElseThrow(() -> new IllegalAccessError("해당글 없음" + bno));
		String toolsCombined = String.join(",", boardUpdateRequestDto.getTools());
		board.modify(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent(), 
				boardUpdateRequestDto.getPayType(),
				boardUpdateRequestDto.getPayAmount(), boardUpdateRequestDto.getCareer(),
				toolsCombined, boardUpdateRequestDto.getExpiredDate(),boardUpdateRequestDto.getWorker(), boardUpdateRequestDto.getYWhen());
		BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
		
		for(Long i: boardUpdateRequestDto.getBoardAttachIds()) {
			BoardAttach boardAttach = attachRepository.findById(i).orElseThrow(() -> new IllegalAccessError("해당 이미지 없음 "+i));
			if(!boardAttach.isFlag()) {
				File temp = new File(boardAttach.getTempPath());
				File dest = new File(boardAttach.getUploadPath());
				try {
					Files.move(temp, dest);
				} catch (IOException e) {
					e.printStackTrace();
				}
				boardAttach.completelySave();
			}
			boardAttach.addBoard(board);
			attachRepository.save(boardAttach);
		}
		
		for(String i: boardUpdateRequestDto.getBoardAttachToBeDeleted()) {
			Optional<BoardAttach> optBoardAttach = attachRepository.findByFileName(i);
			if(optBoardAttach.isPresent()) {
				BoardAttach boardAttach = optBoardAttach.get();
				boardAttach.deleteByFlag();
				
				File toDel = new File(boardAttach.getUploadPath());
				if(toDel.exists()) {
					toDel.delete();				
				} else {
					System.out.println("Such File does not exist!");
				}
				attachRepository.save(boardAttach);
				
			}
		}
		Optional<Thumbnail> origThumb = thumbnailRepository.findById(boardUpdateRequestDto.getThumbnailId());
		if(origThumb.isPresent()) {
			Thumbnail origThumbnail = origThumb.get();
			if(boardUpdateRequestDto.getThumbnailId() != origThumbnail.getThumbnailId()) {
				File thumbToDel = new File(origThumbnail.getUploadPath());
				if(thumbToDel.exists()) {
					thumbToDel.delete();
				} else {
					/***
					 * 파일 존재하지 않을시 예외처리 필요
					 */
				}
				File origToDel = new File(origThumbnail.getOriginalFileDest());
				if(origToDel.exists()) {
					origToDel.delete();
				} else {
					/***
					 * 파일 존재하지 않을시 예외처리 필요
					 */
				}
				
				if(boardUpdateRequestDto.getThumbnailId() != null && boardUpdateRequestDto.getThumbnailId() != 0L) {
					Optional<Thumbnail> newThumb = thumbnailRepository.findById(boardUpdateRequestDto.getThumbnailId());
					if(newThumb.isPresent()) {
						if(!newThumb.get().isFlag()) {
							File temp = new File(newThumb.get().getTempPath());
							File dest = new File(newThumb.get().getUploadPath());
							File origTemp = new File(newThumb.get().getOriginalFileTemp());
							File origDest = new File(newThumb.get().getOriginalFileDest());
							try {
								Files.move(temp, dest);
								Files.move(origTemp, origDest);
							} catch (IOException e) {
								e.printStackTrace();
							}
							newThumb.get().completelySave();
						}
						newThumb.get().addBoard(board);
						thumbnailRepository.save(newThumb.get());		
					}
				}
			}
		}
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
			List<String> tools = new ArrayList<>();
			if(board.getTools() != null) {
				tools = Arrays.asList(board.getTools().split(","));
			}
			dto.setTools(tools);
			dto.setLikesAndComments(likes, comments);
			/*
			 * 유저 아이디 값을 받도록 바꾸어야 한다.
			 * */
			Optional<Thumbnail> thumbnail = thumbnailRepository.findByBoardBoardId(board.getBoardId());
			if(thumbnail.isPresent()) {
				dto.setThumbnail(thumbnail.get().getUploadPath());
				/***
				 * boardsInBoardType 등 리스트를 뽑아 보낼때는 썸네일 테이블에서 리사이즈한 파일을 보내준다
				 */ 				
			}
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
			List<String> tools = new ArrayList<>();
			if(board.getTools() != null) {
				tools = Arrays.asList(board.getTools().split(","));
			}
			dto.setTools(tools);
			dto.setLikesAndComments(likes, comments);
			dto.setLiked(likedOrNot);
			
			Optional<Thumbnail> thumbnail = thumbnailRepository.findByBoardBoardId(board.getBoardId());
			if(thumbnail.isPresent()) {
				dto.setThumbnail(thumbnail.get().getUploadPath());
			}
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
			List<String> tools = new ArrayList<>();
			if(board.getTools() != null) {
				tools = Arrays.asList(board.getTools().split(","));
			}
			dto.setTools(tools);
			dto.setLikesAndComments(likes, comments);
			dto.setLiked(likedOrNot);
			
			Optional<Thumbnail> thumbnail = thumbnailRepository.findByBoardBoardId(board.getBoardId());
			if(thumbnail.isPresent()) {
				dto.setThumbnail(thumbnail.get().getUploadPath());
			}
			
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
			List<String> tools = new ArrayList<>();
			if(board.getTools() != null) {
				tools = Arrays.asList(board.getTools().split(","));
			}
			dto.setTools(tools);
			dto.setLikesAndComments(likes, comments);
			dto.setLiked(likedOrNot);
			
			Optional<Thumbnail> thumbnail = thumbnailRepository.findByBoardBoardId(board.getBoardId());
			if(thumbnail.isPresent()) {
				dto.setThumbnail(thumbnail.get().getUploadPath());
			}
			
			curBoardResponseDto.add(dto);
		}
		return curBoardResponseDto;
	}
}
