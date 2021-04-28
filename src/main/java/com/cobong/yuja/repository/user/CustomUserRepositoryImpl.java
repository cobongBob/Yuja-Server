package com.cobong.yuja.repository.user;

import java.util.List;

import static com.cobong.yuja.model.QUser.user;
import static com.cobong.yuja.model.QAuthorities.authorities;

import com.cobong.yuja.model.Authorities;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository{
	private JPAQueryFactory queryFactory;
	
	@Override
	public List<Authorities> authoritiesUser(Long userId) {
		return queryFactory.selectFrom(authorities)
				.where(authorities.user.userId.eq(userId))
				.orderBy(user.createdDate.asc()).fetch();
	}
	
	
	

}
