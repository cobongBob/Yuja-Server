package com.cobong.yuja.repository.user;

import static com.cobong.yuja.model.QAuthorities.authorities;
import static com.cobong.yuja.model.QUser.user;

import java.util.List;

import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.response.UserFckingDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<User> findByEmail(String username) {
		return queryFactory.selectFrom(user).where(user.username.eq(username)).fetch();
	}

	@Override
	public List<UserFckingDto> userAuthorities(Long authorityId) {
		System.out.println("ddddddddddddddddddddddddddd");
			return queryFactory
					.select(Projections.constructor(UserFckingDto.class, user.username, 
							authorities.authorityId, user.userId ))
					.from(user)
					.join(authorities).on(authorities.user.userId.eq(user.userId))
					.fetchJoin()
					.where(authorities.authorityId.eq(authorityId))
					.fetch();
	}

	@Override
	public List<Authorities> authoritiesUser(Long id) {
		
		return null;
	}
}
