package com.cobong.yuja.payload.response.board;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import com.cobong.yuja.model.BoardType;
import com.cobong.yuja.payload.response.user.UserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardTypeResponseDto {
	private Long boardCode;
	private String boardName;
	
	public BoardTypeResponseDto entityToDto(BoardType entity) {
		return BoardTypeResponseDto.builder()
				.boardCode(entity.getBoardCode())
				.boardName(entity.getBoardName())
				.build();
	}
}
