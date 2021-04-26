package com.cobong.yuja.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BoardAttach {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name ="boardId")
	@JsonManagedReference
	private Board board;
	
	@Column(nullable = false)
	private String uploadPath;
	
	/*
	 * @Enumerated(EnumType.STRING)
	private FileUploadPaths uploadPath;
	 * */
	
	@Column(nullable = false)
	private String fileName;
}