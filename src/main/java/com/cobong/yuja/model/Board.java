package com.cobong.yuja.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.cobong.yuja.model.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(exclude = {"attachments","comments","boardType","user"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board extends DateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //increment
	private Long boardId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boardCode")	
	private BoardType boardType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@Column(nullable = false)
	private String title;
	
	@Lob
	@Column(nullable = false)
	private String content;
	
	@OneToMany(mappedBy = "board",cascade = CascadeType.REMOVE)
	private List<BoardAttach> attachments = new ArrayList<BoardAttach>();
	
	@OneToMany(mappedBy = "board",cascade = CascadeType.REMOVE)
	private List<BoardLiked> liked = new ArrayList<BoardLiked>();
	
	@OneToMany(mappedBy = "board",cascade = CascadeType.REMOVE)
	private List<BoardComment> comments = new ArrayList<BoardComment>();
	
	@Column(nullable = false)
	private int hit; 
	
	@OneToMany(mappedBy = "board",cascade = CascadeType.REMOVE)
	private List<Thumbnail> thumbnail = new ArrayList<Thumbnail>();

	/*
	 * 삭제 되는 시간을 일단위로 받음. 자동으로 새벽 3,4 시 쯤 확인하는 함수를 만들어 구현?
	 * */
	private Date expiredDate;
	
	private String payType;
	
	private String payAmount;
	
	private String career;
	
	private String tools;
	
	private String channelName;
	
	private int recruitingNum;
	
	private String receptionMethod;
	
	private String manager;
	
	private String worker;
	
	private String yWhen;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isPrivate;
	
	private String previewImage;
	
	@Column(nullable = false)
	private Instant boardUpdatedDate;
	
	public Board modify(String title, String content, String payType, String payAmount,	String career, String tools, 
			Date expiredDate, String worker, String yWhen, String channelName, int recruitingNum,String receptionMethod,
			String manager, boolean hidden, String previewImage,Instant boardUpdatedDate) {
		
		this.title=title;
		this.content=content;
		this.payType=payType;
		this.payAmount=payAmount;
		this.career=career;
		this.tools=tools;
		this.expiredDate=expiredDate;
		this.worker=worker;
		this.yWhen = yWhen;
		this.channelName = channelName;
		this.recruitingNum = recruitingNum;
		this.receptionMethod = receptionMethod;
		this.manager = manager;
		this.isPrivate = hidden;
		this.previewImage=previewImage;
		this.boardUpdatedDate=boardUpdatedDate;
		return this;
	}

	public Board createBoard(BoardType boardType, User user, String title, String content, Date expiredDate, String payType,
			String payAmount, String career, String tools, String worker, String yWhen, String channelName, int recruitingNum,
			String receptionMethod,String manager, boolean hidden, String previewImage,Instant boardUpdatedDate) {
		Board board = new Board();
		board.boardId = boardId;
		board.boardType = boardType;
		board.user = user;
		board.title = title;
		board.content = content;
		board.expiredDate = expiredDate;
		board.payType = payType;
		board.payAmount = payAmount;
		board.career = career;
		board.tools = tools;
		board.worker =worker;
		board.yWhen = yWhen;
		board.channelName = channelName;
		board.recruitingNum = recruitingNum;
		board.receptionMethod = receptionMethod;
		board.manager = manager;
		board.isPrivate = hidden;
		board.previewImage=previewImage;
		board.boardUpdatedDate=boardUpdatedDate;
		return board;
	}

	public void addHit() {
		this.hit = this.hit+1;
	}
	
	
}