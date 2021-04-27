package com.cobong.yuja.payload.request;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardAttach;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
