package com.cobong.yuja.payload.response.board;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.response.user.UserResponseDto;

import lombok.Getter;

@Getter
public class BoardResponseDto {
	private Long id;
	private UserResponseDto user;
	private String title;
	private String content;
	private int hit; 
	private String thumbnail;
	private Date expiredDate;
	private String payType;
	private String payAmount;
	private String career;
	private String tools;
	private int likes;
	private int comments;
	private boolean liked;
	private Instant updatedDate;
	private List<Long> boardAttachIds;
	private String channelName;
	private int recruitingNum;
	private String receptionMethod;
	private String portfolioFormat;
	private String manager;
	
	public BoardResponseDto entityToDto(Board entity) {
		this.id=entity.getBoardId();
		this.user = new UserResponseDto().entityToDto(entity.getUser());
		this.title=entity.getTitle();
		this.content=entity.getContent();
		this.hit=entity.getHit();
		this.thumbnail=entity.getThumbnail();
		this.expiredDate=entity.getExpiredDate();
		this.payType=entity.getPayType();
		this.payAmount=entity.getPayAmount();
		this.career=entity.getCareer();
		this.tools=entity.getTools();
		this.updatedDate = entity.getUpdatedDate();
		this.channelName = entity.getChannelName();
		this.recruitingNum = entity.getRecruitingNum();
		this.receptionMethod = entity.getReceptionMethod();
		this.manager = entity.getManager();
		return this;
	}
	
	public void setLikesAndComments(int likes, int comments, boolean likedOrNot) {
		this.likes = likes;
		this.comments = comments;
		this.liked = likedOrNot;
	}
}
