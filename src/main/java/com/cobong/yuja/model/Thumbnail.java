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
import lombok.ToString;

@Getter
@ToString(exclude = {"user"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Thumbnail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long thumbnailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Board board;
	
	@Column(nullable = false)
	private String uploadPath;
	
	@Column(nullable = false)
	private String tempPath;
	
    @Column(nullable = false)
    private String origFilename;
    
	@Column(nullable = false)
	private String fileName;
	
	@Column(nullable = false)
	private boolean flag;
	
	public void completelySave() {
		this.flag = true;
	}
	public void addUser(Board board) {
		this.board = board;
	}
	public void deleteByFlag() {
		this.flag = false;
	}
}
