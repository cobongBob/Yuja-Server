package com.cobong.yuja.payload.request.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardLikedRequestDto {
	private Long userId;
	private Long boardId;
}
