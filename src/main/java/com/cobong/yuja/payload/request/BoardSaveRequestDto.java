package com.cobong.yuja.payload.request;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardAttach;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardSaveRequestDto {
	String title;
	String content;
	List<BoardAttach> attache;
	String thumbnail;
	String payType = "";
	String payAmount = "";
	String career = "";
	String tools = "";
	int hit = 0;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date expiredDate;
	public Board dtoToEntity() {
		return Board.builder()
				.title(this.title)
				.content(this.content)
				.attachments(this.attache)
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