package com.cobong.yuja.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.cobong.yuja.model.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board extends DateAudit{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //increment
	private int id; 
	
//	@OneToMany
//    @JoinColumn
//	private BoardType boardCode;
//	
//	@OneToMany
//    @JoinColumn
//	private int userId;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob
	private String content;
	
	@Column
	private int hit; 
	
	@Column
	private String payType;
	
	@Column
	private String payAmount;
	
	@Column
	private String thumbnail;
	
	@Column
	private String career;
	
	@Column
	private String tools;
	

}