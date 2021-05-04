package com.cobong.yuja.payload.request.board;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.cobong.yuja.model.Board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequestDto {
	private String title;
	private String content;
	private String thumbnail;
	private String payType = "";
	private String payAmount = "";
	private String career = "";
	private List<String> tools = new ArrayList<String>();
	private int hit = 0;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expiredDate;
	private String channelName = "";
	private int recruitingNum = 0;
	private String receptionMethod = "";
	private String manager = "";
	private List<Long> boardAttachIds;
	private List<String> boardAttachToBeDeleted;
	
	public Board dtoToEntity() {
		return Board.builder()
				.title(this.title)
				.content(this.content)
				.thumbnail(this.thumbnail)
				.payType(this.payType)
				.payAmount(this.payAmount)
				.career(this.career)
				.hit(this.hit)
				.expiredDate(this.expiredDate)
				.channelName(this.channelName)
				.recruitingNum(this.recruitingNum)
				.receptionMethod(this.receptionMethod)
				.manager(this.manager)
				.build();
	}
}
