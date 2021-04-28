package com.cobong.yuja.payload.response;

import java.util.ArrayList;
import java.util.List;

import com.cobong.yuja.model.BoardComment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
	private Long cId;
	private String content;
	private Long userId;
	private String nickname;
//	private String updateDate;
	private List<CommentResponseDto> children = new ArrayList<>();
	
	public CommentResponseDto(Long cId, String content, Long userId, String nickname) {
		this.cId = cId;
		this.content = content;
		this.userId = userId;
		this.nickname = nickname;
	}
	
	public CommentResponseDto entityToDto(BoardComment comment) {
		return new CommentResponseDto(comment.getCommentId(), comment.getContent(), comment.getUser().getUserId(), comment.getUser().getNickname());
	}
}
