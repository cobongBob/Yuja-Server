package com.cobong.yuja.repository.board;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cobong.yuja.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, CustomBoardRepository {

	// 유튜버(1) 에디터(2) 썸넬러(3) 최신순(updatedDate)   4개 
	@Query(nativeQuery = true, value ="SELECT * FROM board b WHERE boardCode = 1 order by updatedDate desc LIMIT 4")
	List<Board> orderYouLatest();
	
	@Query(nativeQuery = true, value ="SELECT * FROM board b WHERE boardCode = 2 order by updatedDate desc LIMIT 4")
	List<Board> orderEditLatest();
	
	@Query(nativeQuery = true, value ="SELECT * FROM board b WHERE boardCode = 3 order by updatedDate desc LIMIT 4")
	List<Board> orderThumLatest();
	
	
	//윈윈(4) 콜라보(5) 최신순(createdDate) 5개
	@Query(nativeQuery = true, value ="SELECT * FROM board b WHERE boardCode = 4  order by createdDate desc LIMIT 5")
	List<Board> orderWinLatest();
	
	@Query(nativeQuery = true, value ="SELECT * FROM board b WHERE boardCode = 5  order by createdDate desc LIMIT 5")
	List<Board> orderColLatest();
	
	
	// 에디터(2) 썸넬러(3) 좋아요순(likes) 12개 
	@Query(nativeQuery = true, value = "SELECT * FROM board b LEFT JOIN (SELECT bl.boardId,COUNT(bl.boardId) AS cnt FROM boardliked bl GROUP BY boardId) AS likeNum ON b.boardId = likeNum.boardId WHERE boardcode=2 ORDER BY cnt DESC LIMIT 12")
	List<Board> orderEditLiked();
	
	@Query(nativeQuery = true, value = "SELECT * FROM board b LEFT JOIN (SELECT bl.boardId,COUNT(bl.boardId) AS cnt FROM boardliked bl GROUP BY boardId) AS likeNum ON b.boardId = likeNum.boardId WHERE boardcode=3 ORDER BY cnt DESC LIMIT 12")
	List<Board> orderThumLiked();
	
	
	@Query("SELECT b FROM Board b WHERE title =:title AND b.user.userId =:userId")
	Optional<Board> findByTitleAndWriter(@Param("title") String title, @Param("userId") Long userId);
}
