package com.cobong.yuja.repository.liked;

import com.cobong.yuja.model.BoardLiked;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.cobong.yuja.model.QBoardLiked.boardLiked;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBoardLikedRepositoryImpl implements CustomBoardLikedRepository{
	private final JPAQueryFactory queryFactory;
	
	@Override
	public String like(Long userId, Long boardId) {
		List<BoardLiked> check = queryFactory.selectFrom(boardLiked)
				.where(boardLiked.board.boardId.eq(boardId), boardLiked.user.userId.eq(userId))
				.fetch();
		if(check.size() == 0) {
			queryFactory.insert(boardLiked).columns(boardLiked.createdDate,boardLiked.updatedDate ,boardLiked.board.boardId, boardLiked.user.userId).values(boardId, userId).execute();
			return "Successfully Added!";
		} else {
			queryFactory.delete(boardLiked).where(boardLiked.likedId.eq(check.get(0).getLikedId())).execute();
			return "Successfully Deleted!";
		}
	}
}