package com.cobong.yuja.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.cobong.yuja.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
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
	
	/*
	 * 디폴트 값을 General로 주는 방법 찾아보기
	 * sts 가 주는 힌트는 @Builder.Default를 이용하라는 듯 하다.  
	 * */
	@OneToMany(mappedBy = "user")
	@JsonIgnoreProperties({"user"})
	private List<Authorities> authorities = new ArrayList<Authorities>();
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, unique = true)
	private String nickname;
	
	@Column(nullable = false)
	private String realName;
	
	@Column(nullable = false)
	private String bday;
	
	/* 현제 다대일 양방향으로 구현되어 있음. 양방향으로 할지, 단방향으로 할지 정하고 확인 후 커밋!!! 
	 * 일반적으로는 접근의 용이함, 편리성 때문에 양방향을 선호하는 듯
	 * 다만 양방향의 경우 발생하는 순환 참조를 해결해야 한다 ==> 
	 * */
	@OneToMany(mappedBy = "user")
	@JsonIgnoreProperties({"user"})
	private List<Board> boards;
	
	@Column(nullable = false, length = 1000)
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

	public void addAuthorities(Authorities authority) {
		authority.setUser(this);
		getAuthorities().add(authority);
	}	
	
	public void addBoard(Board board) {
		board.setUser(this);
		getBoards().add(board);
	}
}
