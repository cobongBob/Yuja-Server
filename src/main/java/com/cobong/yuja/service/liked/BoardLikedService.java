package com.cobong.yuja.service.liked;

public interface BoardLikedService {
	String liked(Long bno, Long userId);
	String disLiked(Long bno, Long userId);
}
