package com.cobong.yuja.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BoardType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardCode;
	
	@Column(nullable = false)
	private String boardName;
	
	@OneToMany(mappedBy = "boardType")	
	@JsonBackReference
	private List<Board> boards = new ArrayList<Board>();
}