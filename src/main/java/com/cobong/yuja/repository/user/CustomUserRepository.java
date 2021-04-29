package com.cobong.yuja.repository.user;

import java.util.List;

import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.response.UserFckingDto;

public interface CustomUserRepository {
	
	List<User> findByEmail(String username);
	
	List<UserFckingDto> userAuthorities(Long authorityId );
	List<Authorities> authoritiesUser(Long id);
}
