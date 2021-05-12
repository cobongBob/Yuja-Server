package com.cobong.yuja.payload.response.board;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.ReportedBoards;
import com.cobong.yuja.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportedBoardsResponseDto {
	private Long reportedBoardsId;
	private Board board;
	private User user;
	private String reportedReason;
	
	public ReportedBoardsResponseDto entitytoDto(ReportedBoards entity){
		this.reportedBoardsId = entity.getReportedBoardsId();
		this.board = entity.getBoard();
		this.user = entity.getUser();
		this.reportedReason = entity.getReportedReason();
		return this;
	}
}
