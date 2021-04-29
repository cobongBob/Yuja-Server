package com.cobong.yuja.model;

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
	private List<BoardComment> comments = new ArrayList<BoardComment>();
	
	@Column(nullable = false)
	private int hit; 
	
	@Column(nullable = false)
	private String thumbnail;

	/*
	 * 삭제 되는 시간을 일단위로 받음. 자동으로 새벽 3,4 시 쯤 확인하는 함수를 만들어 구현?
	 * */
	@Column(nullable = true)
	private Date expiredDate;
	
	@Column(nullable = true)
	private String payType;
	
	@Column(nullable = true)
	private String payAmount;
	
	@Column(nullable = true)
	private String career;
	
	@Column(nullable = true)
	private String tools;

	public Board modify(String title, String content, String thumbnail, String payType, String payAmount,
			String career, String tools, Date expiredDate) {
		
		this.title=title;
		this.content=content;
		this.thumbnail=thumbnail;
		this.payType=payType;
		this.payAmount=payAmount;
		this.career=career;
		this.tools=tools;
		this.expiredDate=expiredDate;
		
		return this;
	}

	public Board createBoard(BoardType boardType, User user, String title, String content,
			String thumbnail, Date expiredDate,
			String payType, String payAmount, String career, String tools) {
		/***
		 * BoarCode 넣어야함!
		 */
		Board board = new Board();
		board.boardId = boardId;
		board.boardType = boardType;
		board.user = user;
		board.title = title;
		board.content = content;
		board.thumbnail = thumbnail;
		board.expiredDate = expiredDate;
		board.payType = payType;
		board.payAmount = payAmount;
		board.career = career;
		board.tools = tools;
		
		return board;
	}
	
	
}