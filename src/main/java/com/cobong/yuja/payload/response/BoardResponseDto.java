package com.cobong.yuja.payload.response;

import java.time.Instant;
import java.util.Date;

import com.cobong.yuja.model.Board;

import lombok.Getter;

@Getter
public class BoardResponseDto {
	private Long id;
//	private BoardType boardType;
	private UserResponseDto user;
	private String title;
	private String content;
//	private List<BoardAttach> attachments = new ArrayList<BoardAttach>();
//	private List<BoardComment> comments = new ArrayList<BoardComment>();
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
		
		return this;
	}
	
	public void setLikesAndComments(int likes, int comments, boolean likedOrNot) {
		this.likes = likes;
		this.comments = comments;
		this.liked = likedOrNot;
	}
}
