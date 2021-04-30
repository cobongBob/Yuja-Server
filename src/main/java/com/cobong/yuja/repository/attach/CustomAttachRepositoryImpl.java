package com.cobong.yuja.repository.attach;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAttachRepositoryImpl implements CustomAttachRepository {
	private final JPAQueryFactory queryFactory;
	
}
