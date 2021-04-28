package com.cobong.yuja.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
	private String content;
	private Long userId;
	private Long boardId;
	private Long parentId;
}
