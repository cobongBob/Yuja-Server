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
@ToString(exclude = {"board"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BoardAttach {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="boardId")
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
	//파일이 업로드되어 있는지 아닌지를 알려주는 불리언 변수
	
	public void completelySave() {
		this.flag = true;
	}
	public void addBoard(Board board) {
		this.board = board;
	}
	public void deleteByFlag() {
		this.flag = false;
	}
}