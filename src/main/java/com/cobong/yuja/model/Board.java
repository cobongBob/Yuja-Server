package com.cobong.yuja.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.cobong.yuja.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board extends DateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //increment
	private Long boardId;
	
	@ManyToOne
	@JoinColumn(name = "boardCode")	
	private BoardType boardType;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@Column(nullable = false)
	private String title;
	
	@Lob
	@Column(nullable = false)
	private String content;
	
	@OneToMany(mappedBy = "board")
	@JsonIgnoreProperties({"board"})
	private List<BoardAttach> attachments = new ArrayList<BoardAttach>();
	
	@OneToMany(mappedBy = "board")
	@JsonIgnoreProperties({"board"})
	private List<BoardComment> comments = new ArrayList<BoardComment>();
	
	@Column(nullable = false)
	private int hit; 
	
	@Column(nullable = false)
	private String thumbnail;

	/*
	 * 삭제 되는 시간을 일단위로 받음. 자동으로 새벽 3,4 시 쯤 확인하는 함수를 만들어 구현?
	 * */
	private Date expiredDate;
	
	private String payType;
	
	private String payAmount;
	
	private String career;
	
	private String tools;
	
	public void addAttachments(BoardAttach attachment) {
		attachment.setBoard(this);
		getAttachments().add(attachment);
	}
	
	public void addComments(BoardComment comment) {
		comment.setBoard(this);
		getComments().add(comment);
	}

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
}