package com.cobong.yuja.repository.board;

import java.util.Date;
import java.util.List;

import com.cobong.yuja.model.Board;

public interface CustomBoardRepository {
	List<Board> boardsUserLiked(Long userId); // 좋아요 누른글 
	List<Board> boardsInBoardType(Long boardCode); // 특정 게시판의 board 목록 
	List<Board> boardsUserWrote(Long userId, Long boardCode); // 유저가 작성한 board 목록
	List<Board> boardsUserCommented(Long userId); // 유저가 댓글단 board목록
	List<Board> boardsSearched(String search); //재목으로 검색한 board 목록
	Long likedReceived(Long boardId); // 좋아요 수 구하는 함수
	Long commentsReceived(Long boardId); // 댓글 갯수 구하는 함수
	
	List<Board> findExpired(Date now); //마감일이 현재시각 보다 이전인 유튜브 공고글 지우기.
	boolean likedOrNot(Long userId, Long boardId);
}
