package com.cobong.yuja.service;

import com.cobong.yuja.payload.request.BoardLikedRequestDto;

public interface BoardLikedService {
	String liked(BoardLikedRequestDto dto);
	String disLiked(BoardLikedRequestDto dto);
}
