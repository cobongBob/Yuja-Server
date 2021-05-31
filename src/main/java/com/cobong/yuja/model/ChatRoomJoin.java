package com.cobong.yuja.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomJoin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roomJoinId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roomId")
	private ChatRoom chatRoom; 
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean deleted;
	
	public void setDeleted(boolean bool) {
		this.deleted = bool;
	}
}
