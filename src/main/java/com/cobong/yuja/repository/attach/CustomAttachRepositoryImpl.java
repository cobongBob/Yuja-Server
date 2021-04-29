package com.cobong.yuja.repository.attach;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.cobong.yuja.model.BoardAttach;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomAttachRepositoryImpl extends QuerydslRepositorySupport {
	private final JPAQueryFactory queryFactory;
	
	public CustomAttachRepositoryImpl(JPAQueryFactory queryFactory) {
		super(BoardAttach.class);
		this.queryFactory = queryFactory;
	}
}
