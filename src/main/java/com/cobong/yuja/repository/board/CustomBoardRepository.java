package com.cobong.yuja.repository.board;

import java.util.List;

import com.cobong.yuja.model.Board;

public interface CustomBoardRepository {
	List<Board> boradsUserLiked(Long userId); // 좋아요 누른글 
	List<Board> boardsInBoardType(Long boardCode); // 특정 게시판의 board 목록 
	List<Board> boardsUserWrote(Long userId); // 유저가 작성한 board 목록
	List<Board> boardsUserCommented(Long userId); // 유저가 댓글단 board목록
	List<Board> boardsSearched(String search); //재목으로 검색한 board 목록
	Long likedReceived(Long boardId); // 좋아요 수 구하는 함수
	Long commentsReceived(Long boardId); // 댓글 갯수 구하는 함수
}
