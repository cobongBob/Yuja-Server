package com.cobong.yuja.payload.request;

import java.util.ArrayList;
import java.util.List;

import com.cobong.yuja.model.BoardComment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
	private Long cId;
	private String content;
	private Long userId;
	private String nickname;
	private List<CommentRequestDto> children = new ArrayList<>();
	
	public CommentRequestDto(Long cId, String content, Long userId, String nickname) {
		this.cId = cId;
		this.content = content;
		this.userId = userId;
		this.nickname = nickname;
	}
	
	public CommentRequestDto entityToDto(BoardComment comment) {
		return new CommentRequestDto(comment.getCommentId(), comment.getContent(), comment.getUser().getUserId(), comment.getUser().getNickname());
	}
}
