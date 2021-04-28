package com.cobong.yuja.repository.user;

import java.util.List;

import com.cobong.yuja.model.Authorities;

public interface CustomUserRepository {
	List<Authorities> authoritiesUser(Long userId);
}
