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
@ToString(exclude = {"comment","sender","recipient","message","confirm"})
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="socketMsgId")
	private SocketMessage message;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="confirmId")
	private YoutubeConfirm confirm;
	
	// 알림을 보낸 유저 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="senderId")
	private User sender;
	 
	// 알림을 받는 유저 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="recipientId")
	private User recipient;
	
	// 알림 종류 댓글, 채팅
	private String type;
	
	// 알림 확인 일시
	private Date readDate;
	
    // 알림 내용(미정)
//	private String message;
	
	public Notification createNotification(BoardComment comment, SocketMessage message, YoutubeConfirm confirm, 
			User sender, User recipient, String type, Date readDate) {
		Notification notification = new Notification();
		notification.comment = comment;
		notification.message = message;
		notification.confirm = confirm;
		notification.sender = sender;
		notification.recipient = recipient;
		notification.type = type;
		notification.readDate = readDate;
    	return notification;
    }
	
	public void setReadDate(Date date) {
		this.readDate = date;
	}
}
