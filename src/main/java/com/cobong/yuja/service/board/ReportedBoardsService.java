package com.cobong.yuja.service.board;

import java.util.List;

import com.cobong.yuja.payload.request.board.ReportedBoardsRequestDto;
import com.cobong.yuja.payload.response.board.ReportedBoardsResponseDto;

public interface ReportedBoardsService {
	String reported(ReportedBoardsRequestDto dto);
	
	List<ReportedBoardsResponseDto> findAll();

}
