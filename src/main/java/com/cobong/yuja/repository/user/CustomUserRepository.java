package com.cobong.yuja.repository.user;

import java.time.Instant;
import java.util.List;

import com.cobong.yuja.model.User;

public interface CustomUserRepository {
	List<User> findByEmail(String username);
	List<User> usersCreatedAfter(Instant date);
	Long countUsers();
	List<User> getYearOldDeletedUsers();
}