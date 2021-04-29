package com.cobong.yuja.model;

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

@Entity
@Getter
@ToString(exclude = {"user", "authorities"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userRoleId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="userId")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="authorityId")
	private Authorities authorities;
}
