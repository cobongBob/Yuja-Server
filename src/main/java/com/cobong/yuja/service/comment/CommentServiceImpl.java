package com.cobong.yuja.service.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardComment;
import com.cobong.yuja.model.Notification;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.comment.CommentRequestDto;
import com.cobong.yuja.payload.response.comment.CommentResponseDto;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.comment.CommentRepository;
import com.cobong.yuja.repository.notification.NotificationRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final NotificationRepository notificationRepository;

	@Transactional(readOnly = true)
	@Override
	public List<CommentResponseDto> getCommentsByBoardId(Long boardId) {
		List<BoardComment> commentlist = commentRepository.findCommentByBoardId(boardId);
		if(commentlist.size() == 0) {
			return new ArrayList<CommentResponseDto>();
		} else {
			//넘어온 boardid로 해당 board를 영속화 시킨다.
			return makeNestedShape(commentlist);
			//db에서 넘어온 comment들을 정렬시킨다.
		}
	}
	
	/**
	 * comment의 createComment는 comment entity를 생성한다.
	 * 매개변수로는 content,board,user,parent 가 들어간다.
	 * 이게 필요한 이유는 requestdto는 board id, user id, parent id로 따로 들어오기 때문에
	 * 그 id값으로 repository에서 객체를 선택후 매개변수로 넣어줘 객체를 생성하기 위해서이다.
	 */
	@Transactional
	@Override
	public CommentResponseDto save(CommentRequestDto dto) {
		BoardComment comment = new BoardComment().createComment(dto.getContent(),
				boardRepository.findById(dto.getBoardId()).orElseThrow(()->new IllegalArgumentException("존재하지않는 글")),
				userRepository.findById(dto.getUserId()).orElseThrow(()->new IllegalArgumentException("존재하지않는 유저")),
				dto.getParentId() != null? 
						commentRepository.findById(dto.getParentId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 댓글엔 대댓글을 달수 없습니다")):null);
		CommentResponseDto responseDto = new CommentResponseDto().entityToDto(commentRepository.save(comment));
		
		Board board = boardRepository.findById(dto.getBoardId()).orElseThrow(() -> new IllegalAccessError("알림 받는 유저 없음 "+dto.getBoardId()));
		String type = "commentNoti"; 
		User sender = userRepository.findById(responseDto.getUserId()).orElseThrow(() -> new IllegalAccessError("알림 보낸 유저 없음 "+dto.getUserId()));
		
		if(sender.isBanned()) {
			throw new IllegalAccessError("이용이 정지된 계정입니다.");
		}
		
		BoardComment parentComment = null;
		if(comment.getParent() != null) {
			parentComment = commentRepository.findById(comment.getParent().getCommentId()).orElse(null);
		}
		User commentReceiver = null;
		if(parentComment != null) {
			commentReceiver = parentComment.getUser();
		}
		
		Optional<Long> lastNoti = null;
		
		if(parentComment !=null && commentReceiver.getUserId().equals(sender.getUserId())) {
		}else if (parentComment == null && board.getUser().getUserId().equals(sender.getUserId())){
		}else if(parentComment != null && !commentReceiver.getUserId().equals(sender.getUserId())) {
			lastNoti = notificationRepository.findByLastNoti(sender.getUserId(), commentReceiver.getUserId(),"nestedComment");
			if(lastNoti.isPresent()) {
				notificationRepository.deleteById(lastNoti.get());
			}
			Notification notification = new Notification().createNotification(
					commentRepository.findById(responseDto.getCommentId()).orElseThrow(() -> new IllegalAccessError("해당 댓글 없음 "+responseDto.getCommentId())), 
					sender, 
					commentReceiver,	
					"nestedComment",
					null);
			notificationRepository.save(notification);
		}else {
			lastNoti = notificationRepository.findByLastNoti(sender.getUserId(), board.getUser().getUserId(),type);
			if(lastNoti.isPresent()) {
				notificationRepository.deleteById(lastNoti.get());
			}
			Notification notification = new Notification().createNotification(
					commentRepository.findById(responseDto.getCommentId()).orElseThrow(() -> new IllegalAccessError("해당 댓글 없음 "+responseDto.getCommentId())), 
					sender, 
					board.getUser(),	
					type,
					null);
			notificationRepository.save(notification);
		}
		return responseDto;
	}
	
	@Transactional(readOnly = true)
	@Override
	public CommentResponseDto getOneCommentById(Long commentId) {
		CommentResponseDto responseDto = new CommentResponseDto().entityToDto(
				commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 댓글")));
		return responseDto;
	}
	
	@Transactional
	@Override
	public CommentResponseDto modify(Long commentId,CommentRequestDto dto) {
		//userRepository.findById(dto.getUserId()).orElseThrow(()->new IllegalArgumentException("이용이 정지된 계정입니다."));
		/***
		 * 현재 댓글을 수정하려는 유저가 밴되어있는지 확인할 필요가 있나 확신이 안들어서 일단 주석 처리만 해둠.
		 */
		BoardComment boardComment = commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 댓글"));
		//select후 영속화
		CommentResponseDto responseDto = new CommentResponseDto().entityToDto(boardComment.modifyComment(dto.getContent()));
		//dirty checking으로 수정후 responseDto로 변환
		return responseDto;
	}
	
	@Transactional
	@Override
	public String deleteById(Long commentId, Long userId) {
		BoardComment boardComment = commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 댓글"));
		
		User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저입니다"));
		if(user.isBanned()) {
			throw new IllegalAccessError("이용이 정지된 계정입니다.");
		}
		
		List<BoardComment> childComments = commentRepository.findByIdParentId(boardComment.getCommentId()).orElse(null);
		//select후 영속화
		if(childComments != null && childComments.size() > 0) {
			//가지고온 댓글을 확인.
			//select * from boardComment where parentId = boardComment.commentId(해당댓글)
			//해당 댓글을 부모로 가진 댓글들을 가지고 온다.
			//null이 아니면 deleted로 바꾼다.
			boardComment.deleteComment();
		} else {
			notificationRepository.deleteByCommentId(commentId);
			commentRepository.deleteById(commentId);
		}
		//대댓글이있으면 안의 deleted가 true로 바뀌고 아니면 삭제.
		return "deleted";
	}
	
	/**
	 * 댓글을 정렬시킨다.
	 * 1. 먼저 받아온 comment 전부를 for문을 돌린다.
	 * 2. Response형태로 바꾼다
	 * 3. 그걸 해쉬 맵에 comment의 id를 키값으로 객체를 value값으로 먼저 넣어놓는다.
	 * 4. 해당 comment에 parent가 null이 자식 comment면
	 * 5. 맵에 해당 comment의 부모의 아이디로 get해 해당 부모에 자식으로 List에 넣어준다.
	 * 6. 최상위 comment면 result에 넣어준다.
	 */
	@Override
	public List<CommentResponseDto> makeNestedShape(List<BoardComment> comments) {
		List<CommentResponseDto> result = new ArrayList<>();
		Map<Long, CommentResponseDto> map = new HashMap<>();
		comments.stream().forEach(comment -> {
			CommentResponseDto dto = new CommentResponseDto();
			dto = dto.entityToDto(comment);
			map.put(dto.getCommentId(), dto);
			if(comment.getParent() != null) {
				map.get(comment.getParent().getCommentId()).getChildren().add(dto);
			} else {
				result.add(dto);
			}
		});
		return result;
	}
}
