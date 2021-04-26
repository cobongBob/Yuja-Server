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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Authorities {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long authorityId;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AuthorityNames authority;
}
