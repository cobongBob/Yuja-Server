package com.cobong.yuja.config.auth;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cobong.yuja.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class PrincipalDetails implements UserDetails {

	private Long userId;
	
	private String username;
	
	@JsonIgnore
	private String password;
	
	private String realname;
	
	private String bday;
	
	private String nickname;
	
	private String profilePic;
	
	private String address;
	
	private String bsn;
	
	private Collection<? extends GrantedAuthority> authority;
	
	public PrincipalDetails(Long userId, String username, String password, String realname, String bday,
			String nickname, String address, String bsn,
			Collection<? extends GrantedAuthority> authority) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.realname = realname;
		this.bday = bday;
		this.nickname = nickname;
		this.address = address;
		this.bsn = bsn;
		this.authority = authority;
	}
	
	public static PrincipalDetails create(User user) {
		
		List<GrantedAuthority> authority = 
				user.getAuthorities()
				.stream()
				.map(roles -> new SimpleGrantedAuthority(roles.getAuthority().name()))
				.collect(Collectors.toList());

		return new PrincipalDetails(user.getUserId(),user.getUsername(),user.getPassword(), user.getRealName(),
				user.getBday(), user.getNickname(), user.getAddress(), user.getBsn(), 
				authority);
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authority;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrincipalDetails that = (PrincipalDetails) obj;
		return Objects.equals(userId, that.userId);
	}


	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}
}
