package com.cobong.yuja.payload.response.board;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.response.user.UserResponseDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
	private ZonedDateTime createDate;
	private ZonedDateTime updatedDate;
	private List<String> boardAttachFileNames;
	private String channelName;
	private int recruitingNum;
	private String receptionMethod;
	private String portfolioFormat;
	private String manager;
	private String worker;
	private String yWhen;
	private String thumbnail;
	private boolean isPrivate;
	private String previewImage;
	private BoardTypeResponseDto boardType;
	private ZonedDateTime boardUpdatedDate;
	
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
		this.updatedDate = entity.getUpdatedDate().atZone(ZoneId.of("Asia/Seoul"));
		this.channelName = entity.getChannelName();
		this.recruitingNum = entity.getRecruitingNum();
		this.receptionMethod = entity.getReceptionMethod();
		this.manager = entity.getManager();
		this.worker = entity.getWorker();
		this.yWhen = entity.getYWhen();
		this.isPrivate = entity.isPrivate();
		this.createDate = entity.getCreatedDate().atZone(ZoneId.of("Asia/Seoul"));
		this.previewImage = entity.getPreviewImage();
		this.boardType = new BoardTypeResponseDto().entityToDto(entity.getBoardType());
		this.boardUpdatedDate = entity.getBoardUpdatedDate().atZone(ZoneId.of("Asia/Seoul"));
		return this;
	}
	
	public void setLikesAndComments(int likes, int comments) {
		this.likes = likes;
		this.comments = comments;
	}
	
	public void setAttaches(List<String> attaches) {
		this.boardAttachFileNames = attaches;
	}
	
	public void setIsPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public boolean getIsPrivate() {
		return this.isPrivate;
	}
}
