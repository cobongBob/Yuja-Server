package com.cobong.yuja.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.cobong.yuja.model.audit.DateAudit;

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
public class BoardComment extends DateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //increment
	private Long boardCommentId;
	
	@ManyToOne
	@JoinColumn(name = "boardId")	
	private Board board;
	
	@ManyToOne
	@JoinColumn(name = "userId")	
	private User user;
	
	//댓글 길이는 설정을 해야함. 
	@Column(nullable = false, length = 2000)
	private String content;
	
	@Column(nullable = false)
	private boolean deleted;
}
