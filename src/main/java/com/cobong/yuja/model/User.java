package com.cobong.yuja.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.cobong.yuja.model.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User extends DateAudit {
	
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

	

	
}
