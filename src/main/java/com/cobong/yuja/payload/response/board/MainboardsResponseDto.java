package com.cobong.yuja.payload.response.board;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MainboardsResponseDto {
	private List<BoardResponseDto> YouUpdatedOrder4;
	private List<BoardResponseDto> EditUpdatedOrder4;
	private List<BoardResponseDto> ThumUpdatedOrder4;
	private List<BoardResponseDto> WincreatedOrder5;
	private List<BoardResponseDto> ColcreatedOrder5;
	private List<BoardResponseDto> EditLikes12;
	private List<BoardResponseDto> ThumbLikes12;

}