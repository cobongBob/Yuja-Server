package com.cobong.yuja.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.cobong.yuja.model.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@ToString(exclude = {"authorities","boards"})
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
	
	@ManyToMany(fetch = FetchType.EAGER,  cascade = {CascadeType.MERGE, CascadeType.DETACH})
	@JoinTable(name = "UserRole", joinColumns = @JoinColumn(name="userId"), inverseJoinColumns = @JoinColumn(name="authorityId"))
	private List<Authorities> authorities;
	
	/* 현제 다대일 양방향으로 구현되어 있음. 양방향으로 할지, 단방향으로 할지 정하고 확인 후 커밋!!! 
	 * 일반적으로는 접근의 용이함, 편리성 때문에 양방향을 선호하는 듯
	 * 다만 양방향의 경우 발생하는 순환 참조를 해결해야 한다 ==> 
	 * */
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
	private List<Board> boards;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String nickname;
	
	@Column(nullable = false)
	private String realName;
	
	@Column(nullable = false)
	private String bday;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<ProfilePicture> profilePic;
	
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
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isMarketingChecked;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean deleted;
	
	private String youtubeUrl;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean banned;
	
	/*
	 * 유저 삭제 불가의 이유==> 알림이 ManyToOne으로 받고 있으나 유저에 아무 연관관계가 없어 유저가 삭제될시 아무런 행동을 취하지 않는다.
	 * 유저가 삭제되면 해당 유저가 연관된 모든 알림이 지워지는게 맞는듯.
	 * */
	@OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
	private List<Notification> notiSender;

	@OneToMany(mappedBy = "recipient", cascade = CascadeType.REMOVE)
	private List<Notification> notiReceiver;
	
	public void modify(String username2, String nickname2, String realName2, String bday2,
			String providedId2, String provider2, String address2, String phone2, String bsn2, String youtubeUrl, boolean banned) {
		this.username = username2;
		this.nickname = nickname2;
		this.bday = bday2;
		this.providedId = providedId2;
		this.provider = provider2;
		this.address = address2;
		this.phone = phone2;
		this.bsn = bsn2;
		this.youtubeUrl = youtubeUrl;
		this.banned=banned;
	}
	
	public void resetPasword(String password) {
		this.password = password;
	}
	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	public void setDeleted(boolean deleted) {
		this.deleted= deleted;
	}
}
