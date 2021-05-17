package com.cobong.yuja.payload.response.comment;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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
	private Long commentId;
	private String content;
	private Long userId;
	private String nickname;
	private boolean deleted;
	private ZonedDateTime updatedDate;
	private ZonedDateTime createdDate;
	private List<CommentResponseDto> children = new ArrayList<>();
	
	public CommentResponseDto(Long commentId, String content, Long userId, String nickname, boolean deleted, ZonedDateTime updatedDate, ZonedDateTime createdDate) {
		this.commentId = commentId;
		this.content = content;
		this.userId = userId;
		this.nickname = nickname;
		this.deleted = deleted;
		this.updatedDate = updatedDate;
		this.createdDate = createdDate;
	}
	
	public CommentResponseDto entityToDto(BoardComment comment) {
		return new CommentResponseDto(comment.getCommentId(), comment.getContent(), comment.getUser().getUserId(), comment.getUser().getNickname(), comment.isDeleted(), comment.getUpdatedDate().atZone(ZoneId.of("Asia/Seoul")), comment.getCreatedDate().atZone(ZoneId.of("Asia/Seoul")));
	}
}
