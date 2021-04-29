package com.cobong.yuja.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@ToString(exclude = {"userRole"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Authorities {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long authorityId;
	
	@OneToMany(mappedBy = "authorities")
	private List<UserRole> userRole;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AuthorityNames authority;
}
