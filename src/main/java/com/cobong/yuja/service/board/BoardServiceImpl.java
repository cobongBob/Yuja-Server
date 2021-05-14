package com.cobong.yuja.service.board;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.exception.Forbidden403Exception;
import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.AuthorityNames;
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
				dto.getPayType(), dto.getPayAmount(), dto.getCareer(), toolsCombined, dto.getWorker(), dto.getYWhen(),
				dto.getChannelName(),dto.getRecruitingNum(),dto.getReceptionMethod(),dto.getManager(), dto.getIsPrivate());
		Board board2 = boardRepository.save(board);
		//null일경우 처리 필요
		if(dto.getBoardAttachNames() != null) {
			for(String i: dto.getBoardAttachNames()) {
				if(attachRepository.findByFileName(i).isPresent()) {
					BoardAttach boardAttach = attachRepository.findByFileName(i).get();
					if(!boardAttach.isFlag()) {
						try {
							File temp = new File(boardAttach.getTempPath());
							File dest = new File(boardAttach.getUploadPath());
							Files.move(temp, dest);
						} catch (IOException e) {
							e.printStackTrace();
							throw new IllegalAccessError("다시 시도해 주세요");
						}
						boardAttach.completelySave();
					}
					boardAttach.addBoard(board2);
					attachRepository.save(boardAttach);					
				} else {
					throw new IllegalAccessError("해당 이미지가 서버에 존재하지 않습니다");
				}
			}
		}
		
		if(dto.getThumbnailId() != null && dto.getThumbnailId() != 0L) {
			Optional<Thumbnail> thumbnailtodel = thumbnailRepository.findById(dto.getThumbnailId());
			if(thumbnailtodel.isPresent()) {
				Thumbnail thumbnail = thumbnailtodel.get();
				if(!thumbnail.isFlag()) {
					try {
						File temp = new File(thumbnail.getTempPath());
						File dest = new File(thumbnail.getUploadPath());
						File origTemp = new File(thumbnail.getOriginalFileTemp());
						File origDest = new File(thumbnail.getOriginalFileDest());
						Files.move(temp, dest);
						Files.move(origTemp, origDest);
					} catch (IOException e) {
						e.printStackTrace();
						throw new IllegalAccessError("다시 시도해 주세요");
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
	public BoardResponseDto findById(Long bno, Long userId) {
		Board board = boardRepository.findById(bno).orElseThrow(() -> new IllegalAccessError("해당글 없음" + bno));
		
		if(userId != board.getUser().getUserId()) {
			board.modify(board.getTitle(), board.getContent(), board.getPayType(), board.getPayAmount(),
					board.getCareer(), board.getTools(), board.getExpiredDate(), board.getWorker(), board.getYWhen(), 
					board.getChannelName(), board.getHit() + 1, board.getReceptionMethod(), board.getManager(), board.isPrivate());
		}
		
		List<String> tools = new ArrayList<>();
		if(board.getTools() != null) {
			tools = Arrays.asList(board.getTools().split(","));
		} 
		
		int likes = Long.valueOf(boardRepository.likedReceived(board.getBoardId())).intValue();
		int comments = Long.valueOf(boardRepository.commentsReceived(board.getBoardId())).intValue();
		boolean likedOrNot = boardRepository.likedOrNot(board.getBoardId(), userId);
		
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
		
		BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
		dto.setLikesAndComments(likes, comments);
		dto.setAttaches(boardAttachesToSend);
		dto.setTools(tools);
		dto.setLiked(likedOrNot);
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
	public String delete(Long bno, Long userId) {
		Board board = boardRepository.findById(bno)
				.orElseThrow(() -> new IllegalAccessError("해당글 없음" + bno));
		User attemptingUser = userRepository.findById(userId).orElseThrow(() -> new IllegalAccessError("해당 유저가 없습니다."));
		List<Authorities> roles = attemptingUser.getAuthorities(); 
		boolean isAdminOrManager = false;
		for(Authorities auth: roles) {
			if(auth.getAuthority() == AuthorityNames.ADMIN || auth.getAuthority() == AuthorityNames.MANAGER) {
				isAdminOrManager = true;
				break;
			}
		}
		if(board.getUser().getUserId() != userId && !isAdminOrManager) {
			throw new IllegalAccessError("관리자가 아니므로 해당 유저의 정보를 삭제할 수 없습니다");
		}
		
		List<BoardAttach> attaches = attachRepository.findAllByBoardId(bno);
		for(BoardAttach boardAttach: attaches) {
			try {
				File toDel = new File(boardAttach.getUploadPath());
				if(toDel.exists()) {
					toDel.delete();				
				}
			} catch (Exception e) {
				throw new IllegalAccessError("해당 이미지가 서버에 존재하지 않습니다");
			}
		}
		
		Optional<Thumbnail> thumbnailtodel = thumbnailRepository.findByBoardBoardId(bno);
		if(thumbnailtodel.isPresent()) {
			Thumbnail thumbnail = thumbnailtodel.get();
			File thumbToDel = new File(thumbnail.getUploadPath());
			if(thumbToDel.exists()) {
				thumbToDel.delete();
			}
			
			File origToDel = new File(thumbnail.getOriginalFileDest());
			if(origToDel.exists()) {
				origToDel.delete();
			} 
		}
		
		boardRepository.deleteById(bno);
		return "success";
	}

	@Override
	@Transactional
	public BoardResponseDto modify(Long bno, BoardUpdateRequestDto boardUpdateRequestDto, Long userId) {
		Board board = boardRepository.findById(bno)
				.orElseThrow(() -> new IllegalAccessError("해당글 없음" + bno));
		User attemptingUser = userRepository.findById(userId).orElseThrow(() -> new IllegalAccessError("해당 유저가 없습니다."));
		List<Authorities> roles = attemptingUser.getAuthorities(); 
		boolean isAdminOrManager = false;
		for(Authorities auth: roles) {
			if(auth.getAuthority() == AuthorityNames.ADMIN || auth.getAuthority() == AuthorityNames.MANAGER) {
				isAdminOrManager = true;
				break;
			}
		}
		if(board.getUser().getUserId() != userId && !isAdminOrManager) {
			throw new IllegalAccessError("관리자가 아니므로 해당 유저의 정보를 삭제할 수 없습니다");
		}
		
		String toolsCombined = String.join(",", boardUpdateRequestDto.getTools());
		board.modify(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent(), 
				boardUpdateRequestDto.getPayType(),
				boardUpdateRequestDto.getPayAmount(), boardUpdateRequestDto.getCareer(),
				toolsCombined, boardUpdateRequestDto.getExpiredDate(),boardUpdateRequestDto.getWorker(), 
				boardUpdateRequestDto.getYWhen(),boardUpdateRequestDto.getChannelName(),
				boardUpdateRequestDto.getRecruitingNum(),boardUpdateRequestDto.getReceptionMethod(),
				boardUpdateRequestDto.getManager(), boardUpdateRequestDto.getIsPrivate());
		BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
		
		for(Long i: boardUpdateRequestDto.getBoardAttachIds()) {
			BoardAttach boardAttach = attachRepository.findById(i).orElseThrow(() -> new IllegalAccessError("해당 이미지 없음 "+i));
			if(!boardAttach.isFlag()) {
				File temp = new File(boardAttach.getTempPath());
				File dest = new File(boardAttach.getUploadPath());
				try {
					Files.move(temp, dest);
				} catch (IOException e) {
					throw new IllegalAccessError("해당 이미지가 서버에 존재하지 않습니다");
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
				try {
					File toDel = new File(boardAttach.getUploadPath());
					if(toDel.exists()) {
						toDel.delete();				
					}
				} catch (Exception e) {
					throw new IllegalAccessError("해당 이미지가 서버에 존재하지 않습니다");
				}
				attachRepository.save(boardAttach);
			}
		}
		
		Optional<Thumbnail> origThumb = thumbnailRepository.findById(boardUpdateRequestDto.getThumbnailId());
		if(origThumb.isPresent()) {
			Thumbnail origThumbnail = origThumb.get();
			if(boardUpdateRequestDto.getThumbnailId() != origThumbnail.getThumbnailId()) {
				try {
					File thumbToDel = new File(origThumbnail.getUploadPath());
					if(thumbToDel.exists()) {
						thumbToDel.delete();
					}
					File origToDel = new File(origThumbnail.getOriginalFileDest());
					if(origToDel.exists()) {
						origToDel.delete();
					}					
				} catch (Exception e) {
					throw new IllegalAccessError("");
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
	public List<BoardResponseDto> boardsInBoardType(Long boardCode,Long userId){
		List<Board> curBoard = boardRepository.boardsInBoardType(boardCode);
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
