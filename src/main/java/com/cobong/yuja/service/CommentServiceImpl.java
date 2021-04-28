package com.cobong.yuja.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.BoardComment;
import com.cobong.yuja.payload.request.CommentRequestDto;
import com.cobong.yuja.payload.response.CommentResponseDto;
import com.cobong.yuja.repository.UserRepository;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.comment.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;

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
		BoardComment comment = new BoardComment();
		comment.createComment(dto.getContent(),
				boardRepository.findById(dto.getBoardId()).orElseThrow(()->new IllegalArgumentException("존재하지않는 글")),
				userRepository.findById(dto.getUserId()).orElseThrow(()->new IllegalArgumentException("존재하지않는 유저")),
				dto.getParentId() != null? 
						commentRepository.findById(dto.getParentId()).orElseThrow(()->new IllegalArgumentException("존재하지않는 부모")):null);
		
		CommentResponseDto responseDto = new CommentResponseDto().entityToDto(commentRepository.save(comment));
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
		BoardComment boardComment = commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 댓글"));
		//select후 영속화
		CommentResponseDto responseDto = new CommentResponseDto().entityToDto(boardComment.modifyComment(dto.getContent()));
		//dirty checking으로 수정후 responseDto로 변환
		return responseDto;
	}
	
	@Transactional
	@Override
	public String deleteById(Long commentId) {
		commentRepository.deleteById(commentId);
		return "Success";
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
			map.put(dto.getCId(), dto);
			if(comment.getParent() != null) {
				map.get(comment.getParent().getCommentId()).getChildren().add(dto);
			} else {
				result.add(dto);
			}
		});
		return result;
	}

}
