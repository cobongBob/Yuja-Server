package com.cobong.yuja.repository.comment;

import java.util.List;

import com.cobong.yuja.model.BoardComment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.cobong.yuja.model.QBoardComment.*;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {
	private final JPAQueryFactory queryFactory;
	

	@Override
	public List<BoardComment> findCommentByBoardId(Long boardId) {
		return queryFactory.selectFrom(boardComment)
				.leftJoin(boardComment.parent)
				.fetchJoin()
				.where(boardComment.board.boardId.eq(boardId))
				.orderBy(boardComment.parent.commentId.asc().nullsFirst()
						,boardComment.createdDate.asc())
				.fetch();
	}
}