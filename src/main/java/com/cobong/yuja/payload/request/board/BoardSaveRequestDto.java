package com.cobong.yuja.payload.request.board;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.cobong.yuja.model.Board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardSaveRequestDto {
	private String title;
	private String content;
	
	private Long boardCode;
	private Long userId;
	/*
	 * @JsonFormat(shape=JsonFormat.Shape.OBJECT)
	private List<BoardAttach> attache;
	 * */
	
	private String thumbnail;
	private String payType = "";
	private String payAmount = "";
	private String career = "";
	private String tools = "";
	private int hit = 0;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expiredDate;
	
	public Board dtoToEntity() {
		return Board.builder()
				.title(this.title)
				.content(this.content)
				.thumbnail(this.thumbnail)
				.payType(this.payType)
				.payAmount(this.payAmount)
				.career(this.career)
				.tools(this.tools)
				.hit(this.hit)
				.expiredDate(this.expiredDate)
				.build();
	}
}
