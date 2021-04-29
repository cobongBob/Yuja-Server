package com.cobong.yuja.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(exclude = {"boards"})
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
	
	@OneToMany(mappedBy = "boardType",cascade = CascadeType.REMOVE)
	private List<Board> boards = new ArrayList<Board>();
}