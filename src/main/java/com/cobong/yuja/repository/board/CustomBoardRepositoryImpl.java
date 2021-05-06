package com.cobong.yuja.repository.board;

import static com.cobong.yuja.model.QBoard.board;
import static com.cobong.yuja.model.QBoardComment.boardComment;
import static com.cobong.yuja.model.QBoardLiked.boardLiked;

import java.util.List;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardLiked;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBoardRepositoryImpl implements CustomBoardRepository {
	private final JPAQueryFactory queryFactory;

	// BoardType.boardName? boardCode?
	@Override
	public List<Board> boardsInBoardType(Long boardCode) {
		return queryFactory.selectFrom(board).where(board.boardType.boardCode.eq(boardCode))
				.orderBy(board.updatedDate.asc()).fetch();
	}

	@Override
	public List<Board> boardsUserWrote(Long userId) {
		return queryFactory.selectFrom(board).where(board.user.userId.eq(userId)).orderBy(board.updatedDate.asc())
				.fetch();
	}

	@Override
	public List<Board> boardsUserLiked(Long userId) {
		/***
		 * Board와 BoardLiked는 연관관계를 가지고 있지 않기에 QueryDsl 자체적인 leftjoin 만으로는 안되는듯 하다. 그렇기에
		 * 사용한 방법이 연관관계를 가지지 않을 경우에 사용하는 방법이라고 찾은 현재의 방법이다
		 * https://jojoldu.tistory.com/396
		 */

		return queryFactory.selectFrom(board).join(boardLiked).on(boardLiked.board.boardId.eq(board.boardId))
				.fetchJoin().where(boardLiked.user.userId.eq(userId)).fetch();

	}

	@Override
	public List<Board> boardsUserCommented(Long userId) {
		return queryFactory.selectFrom(board).leftJoin(board.comments, boardComment).fetchJoin()
				.where(boardComment.user.userId.eq(userId)).fetch();

	}

	@Override
	public List<Board> boardsSearched(String search) {
		return queryFactory.selectFrom(board).where(board.title.contains(search)).orderBy(board.updatedDate.asc())
				.fetch();

	}

	@Override
	public Long likedReceived(Long boardId) {
		return queryFactory.selectFrom(boardLiked).where(boardLiked.board.boardId.eq(boardId)).fetchCount();

	}

	@Override
	public Long commentsReceived(Long boardId) {
		return (long) queryFactory.selectFrom(boardComment).where(boardComment.board.boardId.eq(boardId)).fetch()
				.size();
	}
	
	@Override
	public boolean likedOrNot(Long boardId, Long userId) {
		List<BoardLiked> check = queryFactory.selectFrom(boardLiked)
				.where(boardLiked.board.boardId.eq(boardId), boardLiked.user.userId.eq(userId))
				.fetch();
		
		if(check.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
}
