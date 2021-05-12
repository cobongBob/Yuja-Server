package com.cobong.yuja.payload.request.board;

import com.cobong.yuja.model.ReportedBoards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportedBoardsRequestDto {
	private Long boardId;
	private Long userId;
	private String reportedReason;

	public ReportedBoards dtoToEntity() {
		return ReportedBoards
				.builder()
				.reportedReason(this.reportedReason)
				.build();
	}
}
