package com.cobong.yuja.service.comment;

import java.util.List;

import com.cobong.yuja.model.BoardComment;
import com.cobong.yuja.payload.request.comment.CommentRequestDto;
import com.cobong.yuja.payload.response.comment.CommentResponseDto;

public interface CommentService {
	
	public List<CommentResponseDto> getCommentsByBoardId(Long boardId);
	public CommentResponseDto save(CommentRequestDto dto);
	public CommentResponseDto getOneCommentById(Long commentId);
	public CommentResponseDto modify(Long commentId, CommentRequestDto dto);
	public String deleteById(Long commentId, Long userId);
	public List<CommentResponseDto> makeNestedShape(List<BoardComment> comments);
}
