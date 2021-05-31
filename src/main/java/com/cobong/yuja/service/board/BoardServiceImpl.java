package com.cobong.yuja.service.board;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardAttach;
import com.cobong.yuja.model.BoardType;
import com.cobong.yuja.model.Notification;
import com.cobong.yuja.model.ProfilePicture;
import com.cobong.yuja.model.Thumbnail;
import com.cobong.yuja.model.User;
import com.cobong.yuja.model.enums.AuthorityNames;
import com.cobong.yuja.payload.request.board.BoardSaveRequestDto;
import com.cobong.yuja.payload.request.board.BoardUpdateRequestDto;
import com.cobong.yuja.payload.response.board.BoardResponseDto;
import com.cobong.yuja.payload.response.board.BoardTypeResponseDto;
import com.cobong.yuja.payload.response.board.MainboardsResponseDto;
import com.cobong.yuja.repository.attach.AttachRepository;
import com.cobong.yuja.repository.attach.ProfilePictureRepository;
import com.cobong.yuja.repository.attach.ThumbnailRepository;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.board.BoardTypeRepository;
import com.cobong.yuja.repository.notification.NotificationRepository;
import com.cobong.yuja.repository.user.AuthoritiesRepository;
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
	private final AuthoritiesRepository authoritiesRepository;
	private final NotificationRepository notificationRepository;
	private final ProfilePictureRepository profilePictureRepository;
	
	@Override
	@Transactional
	public BoardResponseDto save(BoardSaveRequestDto dto) {
		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalAccessError("해당유저 없음 "+dto.getUserId()));
		if(user.isBanned()) {
			throw new IllegalAccessError("이용이 정지된 계정입니다.");
		}
		//나중에 수정필 어드민 아이디 넣어줘야함
		User admin = userRepository.findById(90L).orElse(null);
		//나중에 수정필
		
		BoardType boardType = boardTypeRepository.findById(dto.getBoardCode()).orElseThrow(() -> new IllegalAccessError("해당글 타입 없음" + dto.getBoardCode()));
		
		if(dto.getBoardCode() == 1L) {
			List<Board> curBoards = boardRepository.boardsUserWrote(user.getUserId(), 1L);
			if(curBoards.size() >= 3) {
				delete(curBoards.get(0).getBoardId(), user.getUserId());
			}
		} else if(dto.getBoardCode() == 2L || dto.getBoardCode() == 3L) {
			List<Board> curBoards = boardRepository.boardsUserWrote(user.getUserId(), dto.getBoardCode());
			if(curBoards.size() >= 1) {
				delete(curBoards.get(0).getBoardId(), user.getUserId());
			}
		} else if(dto.getBoardCode() == 8L) {
			if(boardRepository.findByTitleAndWriter(dto.getTitle(), user.getUserId()).isPresent()) {
				throw new IllegalAccessError("해당글 신고 처리중 입니다.");
			}
			
		}
		
		
		String receivelink = dto.getPreviewImage();
		String target = "https://www.youtube.com/watch?v=";
		
		if(receivelink.startsWith(target)) {
			String code = receivelink.substring(target.length(), target.length()+11);
			String previewImage = "https://img.youtube.com/vi/" + code + "/hqdefault.jpg";
			dto.setPreviewImage(previewImage);
		} else {
			String previewImage = "http://localhost:8888/imgs/defaultImg.png";
			dto.setPreviewImage(previewImage);
		}
		
		String toolsCombined = String.join(",", dto.getTools());
		
		Date expDate = dto.getExpiredDate() == null?new Date(1924873200000L):dto.getExpiredDate();
		
		Board board = new Board().createBoard(boardType, user, dto.getTitle(), dto.getContent(), expDate,
				dto.getPayType(), dto.getPayAmount(), dto.getCareer(), toolsCombined, dto.getWorker(), dto.getYWhen(),
				dto.getChannelName(),dto.getRecruitingNum(),dto.getReceptionMethod(),dto.getManager(), dto.getIsPrivate(), 
				dto.getPreviewImage(),Instant.now());
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
		
		// 승격 알림 추가함
		BoardResponseDto boardDto = new BoardResponseDto().entityToDto(board2);
		if(dto.getBoardCode() == 2L) {
			Authorities editor = authoritiesRepository.findByAuthority(AuthorityNames.EDITOR).get();
			if(!user.getAuthorities().contains(editor)) {
				user.getAuthorities().add(editor);
				
				String type = "editNoti"; 
				Notification notification = new Notification().createNotification(
						null, 
						admin, // sender default admin
						userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalAccessError("해당 유저가 존재하지 않습니다.")),
						type,
						null);
				notificationRepository.save(notification);	
			}
		} else if(dto.getBoardCode() == 3L) {
			Authorities thumb = authoritiesRepository.findByAuthority(AuthorityNames.THUMBNAILER).get();
			if(!user.getAuthorities().contains(thumb)) {
				user.getAuthorities().add(thumb);

				String type = "thumbNoti"; 
				Notification notification = new Notification().createNotification(
						null, 
						admin, // sender default admin
						userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalAccessError("해당 유저가 존재하지 않습니다.")),
						type,
						null);
				notificationRepository.save(notification);	
			}
		}
		
		return boardDto;
	}
	
	@Override
	@Transactional
	public BoardResponseDto findById(Long bno, Long userId, boolean ishit) {
		Board board = boardRepository.findById(bno).orElseThrow(() -> new IllegalAccessError("해당글 없음" + bno));
		if(ishit == false&&userId != board.getUser().getUserId()) {
			board.addHit();
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
		String thumbnailOrig = "original";
		if(thumbnailtodel.isPresent()) {
			Thumbnail thumbnail = thumbnailtodel.get();
			if(thumbnail.getOriginalFileDest() != null && thumbnail.getOriginalFileDest().length() != 0) {
				thumbnailOrig += thumbnail.getFileName();
			}
		}
		BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
		Optional<ProfilePicture> optProfile = profilePictureRepository.findByUserUserId(board.getUser().getUserId());
		if(optProfile.isPresent()) {
			dto.setProfilePicture("http://localhost:8888/files/profiles/"+optProfile.get().getFileName());
		} else {
			dto.setProfilePicture("");
		}
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
		if(attemptingUser.isBanned()) {
			throw new IllegalAccessError("이용이 정지된 계정입니다.");
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
		if(attemptingUser.isBanned()) {
			throw new IllegalAccessError("이용이 정지된 계정입니다.");
		}
		
		String receivelink = boardUpdateRequestDto.getPreviewImage();
		String target = "https://www.youtube.com/watch?v=";
		
		if(receivelink.startsWith(target)) {
			String code = receivelink.substring(target.length(), target.length()+11);
			String previewImage = "https://img.youtube.com/vi/" + code + "/hqdefault.jpg";
			boardUpdateRequestDto.setPreviewImage(previewImage);
		} else {
			String previewImage = "http://localhost:8888/imgs/defaultImg.png";
			boardUpdateRequestDto.setPreviewImage(previewImage);
		}
		
		String toolsCombined = String.join(",", boardUpdateRequestDto.getTools());
		board.modify(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent(), 
				boardUpdateRequestDto.getPayType(),
				boardUpdateRequestDto.getPayAmount(), boardUpdateRequestDto.getCareer(),
				toolsCombined, boardUpdateRequestDto.getExpiredDate(),boardUpdateRequestDto.getWorker(), 
				boardUpdateRequestDto.getYWhen(),boardUpdateRequestDto.getChannelName(),
				boardUpdateRequestDto.getRecruitingNum(),boardUpdateRequestDto.getReceptionMethod(),
				boardUpdateRequestDto.getManager(), boardUpdateRequestDto.getIsPrivate(), boardUpdateRequestDto.getPreviewImage(), Instant.now());
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
		
		Optional<Thumbnail> origThumb = thumbnailRepository.findByBoardBoardId(board.getBoardId());
		if(origThumb.isPresent()) {
			Thumbnail origThumbnail = origThumb.get();
			System.out.println(origThumbnail);
			if(boardUpdateRequestDto.getThumbnailId() != origThumbnail.getThumbnailId() && boardUpdateRequestDto.getThumbnailId() != 0L) {
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
					throw new IllegalAccessError("이전 썸네일이 존재하지 않습니다.");
				}
				thumbnailRepository.delete(origThumbnail);
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
								throw new IllegalAccessError("썸네일이 존재하지 않습니다");
							}
							newThumb.get().completelySave();
						}
						newThumb.get().addBoard(board);
						thumbnailRepository.save(newThumb.get());		
					}
				}
			}
		} else {
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
							throw new IllegalAccessError("썸네일이 존재하지 않습니다");
						}
						newThumb.get().completelySave();
					}
					newThumb.get().addBoard(board);
					thumbnailRepository.save(newThumb.get());		
				}
			}
		}
		return dto;
	}
	
	@Override
	@Transactional
	public List<BoardResponseDto> boardsInBoardType(Long boardCode,Long userId){
		List<Board> curBoard = boardRepository.boardsInBoardType(boardCode);
		List<BoardResponseDto> curBoardResponseDto = new ArrayList<BoardResponseDto>();
		BoardType boardType = boardTypeRepository.findById(boardCode).orElseThrow(() -> new IllegalAccessError("존재하지 않는 게시판"));
		for(Board board: curBoard) {
			boolean likedOrNot = boardRepository.likedOrNot(board.getBoardId(), userId);
			int likes = Long.valueOf(boardRepository.likedReceived(board.getBoardId())).intValue();
			int comments = Long.valueOf(boardRepository.commentsReceived(board.getBoardId())).intValue();
			BoardResponseDto dto = new BoardResponseDto().entityToDto(board);
			List<String> tools = new ArrayList<>();
			if(board.getTools() != null && board.getTools().length() != 0) {
				tools = Arrays.asList(board.getTools().split(","));
			}
			dto.setTools(tools);
			dto.setLikesAndComments(likes, comments);
			dto.setLiked(likedOrNot);
			dto.setBoardType(new BoardTypeResponseDto().entityToDto(boardType));
			Optional<Thumbnail> thumbnail = thumbnailRepository.findByBoardBoardId(board.getBoardId());
			if(thumbnail.isPresent()) {
				dto.setThumbnail(thumbnail.get().getFileName());		
			} else {
				dto.setThumbnail("defaultImg.png");
			}
			
			if(boardType.getBoardCode() == 1L) {
				Optional<ProfilePicture> psa = profilePictureRepository.findByUserUserId(board.getUser().getUserId());
				if(psa.isPresent()) {
					dto.setPreviewImage("http://localhost:8888/files/profiles/"+psa.get().getFileName());					
				}
			} 
			
			if(boardType.getBoardCode() == 8L) {
				Long reportedId = Long.valueOf(board.getTitle().substring(board.getTitle().indexOf("##")+2));
				if(!boardRepository.findById(reportedId).isPresent()) {
					System.out.println(board);
					delete(board.getBoardId(), userId);
				}else {
					curBoardResponseDto.add(dto);
				}
			} else {
				curBoardResponseDto.add(dto);				
			}
			
		}
		return curBoardResponseDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BoardResponseDto> boardsUserWrote(Long userId, Long boardCode) {
		List<Board> curBoard = boardRepository.boardsUserWrote(userId, boardCode);
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
				dto.setThumbnail(thumbnail.get().getFileName());
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
				dto.setThumbnail(thumbnail.get().getFileName());
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
				dto.setThumbnail(thumbnail.get().getFileName());
			}
			
			curBoardResponseDto.add(dto);
		}
		return curBoardResponseDto;
	}

	@Override
	@Transactional(readOnly = true)
	public MainboardsResponseDto getMainBoardData() {
		
		// 유튜버(1) 최신순(updatedDate) 
		List<Board> board =boardRepository.orderYouLatest();
		MainboardsResponseDto mainboardsResponseDto = new MainboardsResponseDto();
		List<BoardResponseDto> result = new ArrayList<BoardResponseDto>();
		for (int i = 0; i < board.size(); i++) {
			BoardResponseDto resDto = new BoardResponseDto().entityToDto(board.get(i));
			Optional<ProfilePicture> optprofile = profilePictureRepository.findByUserUserId(board.get(i).getUser().getUserId());
			if(optprofile.isPresent()) {
				resDto.setPreviewImage("http://localhost:8888/files/profiles/"+optprofile.get().getFileName());
			}else {
				resDto.setPreviewImage("");
			}
			result.add(resDto);
		}
		mainboardsResponseDto.setYouUpdatedOrder4(result);
		
		// 에디터(2) 최신순(updatedDate)
		board =boardRepository.orderEditLatest();
		result = new ArrayList<BoardResponseDto>();
		for (int i = 0; i < board.size(); i++) {
			BoardResponseDto resDto = new BoardResponseDto().entityToDto(board.get(i));
			Optional<ProfilePicture> optprofile = profilePictureRepository.findByUserUserId(board.get(i).getUser().getUserId());
			if(optprofile.isPresent()) {
				resDto.setPreviewImage("http://localhost:8888/files/profiles/"+optprofile.get().getFileName());
			}else {
				resDto.setPreviewImage("");
			}
			result.add(resDto);
		}
		mainboardsResponseDto.setEditUpdatedOrder4(result);
		
		// 썸넬러(3) 최신순(updatedDate)
		board =boardRepository.orderThumLatest();
		result = new ArrayList<BoardResponseDto>();
		for (int i = 0; i < board.size(); i++) {
			BoardResponseDto resDto = new BoardResponseDto().entityToDto(board.get(i));
			Optional<ProfilePicture> optprofile = profilePictureRepository.findByUserUserId(board.get(i).getUser().getUserId());
			if(optprofile.isPresent()) {
				resDto.setPreviewImage("http://localhost:8888/files/profiles/"+optprofile.get().getFileName());
			}else {
				resDto.setPreviewImage("");
			}
			result.add(resDto);
		}
		
		mainboardsResponseDto.setThumUpdatedOrder4(result);

		// 윈윈(4) 최신순(createdDate) 5개
		board =boardRepository.orderWinLatest();
		result = new ArrayList<BoardResponseDto>();
		for (int i = 0; i < board.size(); i++) {
			BoardResponseDto resDto = new BoardResponseDto();
			int likes = Long.valueOf(boardRepository.likedReceived(board.get(i).getBoardId())).intValue();
			int comments = Long.valueOf(boardRepository.commentsReceived(board.get(i).getBoardId())).intValue();
			resDto.setLikesAndComments(likes, comments);
			result.add(resDto.entityToDto(board.get(i)));
		}
		mainboardsResponseDto.setWincreatedOrder5(result);
		
		
		// 콜라보(5) 최신순(createdDate) 5개
		board =boardRepository.orderColLatest();
		result = new ArrayList<BoardResponseDto>();
		for (int i = 0; i < board.size(); i++) {
			BoardResponseDto resDto = new BoardResponseDto();
			int likes = Long.valueOf(boardRepository.likedReceived(board.get(i).getBoardId())).intValue();
			int comments = Long.valueOf(boardRepository.commentsReceived(board.get(i).getBoardId())).intValue();
			resDto.setLikesAndComments(likes, comments);
			result.add(resDto.entityToDto(board.get(i)));
		}
		mainboardsResponseDto.setColcreatedOrder5(result);
		
		
		// 에디터(2) 좋아요순(likes) 12개 
		board =boardRepository.orderEditLiked();
		result = new ArrayList<BoardResponseDto>();
		for (int i = 0; i < board.size(); i++) {
			BoardResponseDto resDto = new BoardResponseDto();
			result.add(resDto.entityToDto(board.get(i)));
		}
		mainboardsResponseDto.setEditLikes12(result);
		
		// 썸넬러(3) 좋아요순(likes) 12개 
		board =boardRepository.orderThumLiked();
		result = new ArrayList<BoardResponseDto>();
		for (int i = 0; i < board.size(); i++) {
			BoardResponseDto resDto = new BoardResponseDto();
			resDto = resDto.entityToDto(board.get(i));
			if(thumbnailRepository.findByBoardBoardId(board.get(i).getBoardId()).isPresent()) {
				resDto.setPreviewImage("http://localhost:8888/files/thumbnail/"+thumbnailRepository.findByBoardBoardId(board.get(i).getBoardId()).get().getFileName());
			}
			result.add(resDto);
		}
		mainboardsResponseDto.setThumbLikes12(result);
		
		return mainboardsResponseDto;
	}

	@Override
	@Transactional
	public String noticePrivateSwitch(Long bno) {
		Board board = boardRepository.findById(bno).orElseThrow(()->new IllegalArgumentException("존재하지 않는 글입니다."));
		board.setPrivate(!board.isPrivate());
		return "success";
	}

	@Override
	@Transactional
	public void deleteExpired() {
		Date now = new Date();
		List<Board> expiredBoards = boardRepository.findExpired(now);
		for(Board expBoard:expiredBoards) {
			boardRepository.delete(expBoard);
		}
	}
}
