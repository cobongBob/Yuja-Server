package com.cobong.yuja.payload.request.board;

import java.util.Date;
import java.util.List;

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
public class BoardUpdateRequestDto {
	private String title;
	private String content;
	private String thumbnail;
	@Builder.Default
	private String payType = "";
	@Builder.Default
	private String payAmount = "";
	@Builder.Default
	private String career = "";
	@Builder.Default
	private String tools = "";
	@Builder.Default
	private int hit = 0;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expiredDate;
	
	@Builder.Default
	private String channelName = "";
	@Builder.Default
	private int recruitingNum = 0;
	@Builder.Default
	private String receptionMethod = "";
	@Builder.Default
	private String manager = "";

	private List<Long> boardAttachIds;
	
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
				.channelName(this.channelName)
				.recruitingNum(this.recruitingNum)
				.receptionMethod(this.receptionMethod)
				.manager(this.manager)
				.build();
	}
}
