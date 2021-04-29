package com.cobong.yuja.payload.request.Board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardLikedRequestDto {
	private Long userId;
	private Long boardId;
}
