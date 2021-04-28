package com.cobong.yuja.repository.board;

import static com.cobong.yuja.model.QBoard.board;
import static com.cobong.yuja.model.QBoardLiked.boardLiked;
import static com.cobong.yuja.model.QBoardComment.boardComment;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.QBoardComment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

public class CustomBoardRepositoryImpl extends QuerydslRepositorySupport implements CustomBoardRepository{
	private final JPAQueryFactory queryFactory;
	
	public CustomBoardRepositoryImpl(JPAQueryFactory queryFactory) {
		super(Board.class);
		this.queryFactory = queryFactory;
	}
	
	// BoardType.boardName? boardCode?
	@Override
	public List<Board> boardsInBoardType(Long boardCode) {
		return queryFactory.selectFrom(board)
				.where(board.boardType.boardCode.eq(boardCode))
				.orderBy(board.updatedDate.asc()).fetch();
	}
	
	@Override
	public List<Board> boardsUserWrote(Long userId) {
		return queryFactory.selectFrom(board)
				.where(board.user.userId.eq(userId))
				.orderBy(board.updatedDate.asc()).fetch();
	}
	
	@Override
	public List<Board> boardsUserLiked(Long userId) {
		return queryFactory.selectFrom(board)
				.leftJoin(boardLiked.board, board).fetchJoin()
				.where(boardLiked.user.userId.eq(userId)).fetch();
	}

	@Override
	public List<Board> boardsUserCommented(Long userId) {
		return queryFactory.selectFrom(board)
				.leftJoin(board.comments, boardComment).fetchJoin()
				.where(boardComment.user.userId.eq(userId)).fetch();
	}

	@Override
	public List<Board> boardsSearched(String search) {
		return queryFactory.selectFrom(board)
				.where(board.title.contains(search))
				.orderBy(board.updatedDate.asc()).fetch();
	}

	@Override
	public Long likedReceived(Long boardId) {
		return queryFactory.selectFrom(boardLiked)
				.where(boardLiked.board.boardId.eq(boardId))
				.fetchCount();
	}
	
	@Override
	public Long commentsReceived(Long boardId) {
		return queryFactory.selectFrom(boardComment)
				.where(boardComment.board.boardId.eq(boardId))
				.fetchCount();
	}
}
