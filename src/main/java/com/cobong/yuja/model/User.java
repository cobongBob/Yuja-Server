package com.cobong.yuja.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, unique = true)
	private String nickname;
	
	@Column(nullable = false)
	private String realName;
	
	@Column(nullable = false)
	private String bday;
	
	@Column(nullable = false)
	private String profilePic;
	
	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp createdDate;
	
	@Column(nullable = true)
	private String providedId;
	
	@Column(nullable = true)
	private String provider;
	
	@Column(nullable = true)
	private String address;
	
	@Column(nullable = true)
	private String phone;
	
	@Column(nullable = true)
	private String bsn;
	
	@Column(length = 1000, nullable = true)
	private String youtubeImg;
	
	@Column(nullable = true)
	private String userIp;

	@Builder
	public User(Long userId, String username, String password, String nickname, String realName, String bday,
			String profilePic, Timestamp createdDate, String providedId, String provider, String address, String phone,
			String bsn, String youtube_img, String user_ip) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.realName = realName;
		this.bday = bday;
		this.profilePic = profilePic;
		this.createdDate = createdDate;
		this.providedId = providedId;
		this.provider = provider;
		this.address = address;
		this.phone = phone;
		this.bsn = bsn;
		this.youtubeImg = youtube_img;
		this.userIp = user_ip;
	}
}
