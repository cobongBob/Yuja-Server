package com.cobong.yuja.repository.user;

import static com.cobong.yuja.model.QUser.user;

import java.util.List;

import com.cobong.yuja.model.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<User> findByEmail(String username) {
		return queryFactory
				.selectFrom(user)
				.where(user.username
						.eq(username))
				.fetch();
	}
}
