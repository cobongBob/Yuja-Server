package com.cobong.yuja.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.cobong.yuja.model.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(exclude = {"comment","user"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Notification extends DateAudit{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notiId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="commentId")
	private BoardComment comment;
	
	// 알림을 받는 유저 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="userId")
	private Board user;
	
	// 알림 종류 댓글, 좋아요, 채팅
	private String type;
	
	// 알림 확인 일시
	private Date readDate;
	
	// 알림 내용
	private String message;
}
