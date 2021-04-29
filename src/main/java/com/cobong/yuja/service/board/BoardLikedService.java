package com.cobong.yuja.service.board;

import com.cobong.yuja.payload.request.board.BoardLikedRequestDto;

public interface BoardLikedService {
	String liked(BoardLikedRequestDto dto);
	String disLiked(BoardLikedRequestDto dto);
}
