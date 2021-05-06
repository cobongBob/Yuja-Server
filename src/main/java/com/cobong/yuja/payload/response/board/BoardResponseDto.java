package com.cobong.yuja.payload.response.board;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.response.user.UserResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDto {
	private Long id;
	private UserResponseDto user;
	private String title;
	private String content;
	private int hit; 
	private Date expiredDate;
	private String payType;
	private String payAmount;
	private String career;
	private List<String> tools;
	private int likes;
	private int comments;
	private boolean liked;
	private Instant updatedDate;
	private List<String> boardAttachFileNames;
	private String channelName;
	private int recruitingNum;
	private String receptionMethod;
	private String portfolioFormat;
	private String manager;
	private String worker;
	private String yWhen;
	
	public BoardResponseDto entityToDto(Board entity) {
		this.id=entity.getBoardId();
		this.user = new UserResponseDto().entityToDto(entity.getUser());
		this.title=entity.getTitle();
		this.content=entity.getContent();
		this.hit=entity.getHit();
		this.expiredDate=entity.getExpiredDate();
		this.payType=entity.getPayType();
		this.payAmount=entity.getPayAmount();
		this.career=entity.getCareer();
		this.updatedDate = entity.getUpdatedDate();
		this.channelName = entity.getChannelName();
		this.recruitingNum = entity.getRecruitingNum();
		this.receptionMethod = entity.getReceptionMethod();
		this.manager = entity.getManager();
		this.worker = entity.getWorker();
		this.yWhen = entity.getYWhen();
		return this;
	}
	
	public void setLikesAndComments(int likes, int comments) {
		this.likes = likes;
		this.comments = comments;
	}
	
	public void setAttaches(List<String> attaches) {
		this.boardAttachFileNames = attaches;
	}
}
