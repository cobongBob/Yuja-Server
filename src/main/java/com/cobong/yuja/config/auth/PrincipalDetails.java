package com.cobong.yuja.config.auth;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cobong.yuja.model.User;

import lombok.Getter;

@Getter
public class PrincipalDetails implements UserDetails {
	
	private Long userid;
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	
	public PrincipalDetails(Long userid, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.userid = userid;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}
	
	// 권한 설정 진행중
//	public static PrincipalDetails create(User user) {
//		List<GrantedAuthority> authorities = user.getAuthorities()
//				.stream()
//				.map(null)
//				.collect(Collectors.toList());
//		
//		
//		return new PrincipalDetails(user.getUserId(), user.getUsername(), user.getPassword(),
//				authorities);
//	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
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

}
