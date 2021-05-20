package com.cobong.yuja.model;

import javax.persistence.Column;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"user"})
@Builder
public class YoutubeConfirm extends DateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long youtubeConfirmId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private User user;
	
	@Column(nullable = false)
	private String uploadPath;
	
	@Column(nullable = false)
	private String tempPath;
	
    @Column(nullable = false)
    private String origFilename;
    
	@Column(nullable = false)
	private String fileName;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean flag;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean authorized;
	
	public void completelySave() {
		this.flag = true;
	}
	public void addUser(User user) {
		this.user = user;
	}
	public void deleteByFlag() {
		this.flag = false;
	}
	public void authorizeAsYoutuber() {
		this.authorized = true;
	}
}
