package com.cobong.yuja.repository.liked;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cobong.yuja.model.BoardLiked;

public interface BoardLikedRepository extends JpaRepository<BoardLiked, Long>, CustomBoardLikedRepository{
	@Modifying
	@Query("delete from BoardLiked where (userid=:userid and boardid=:boardid)")
	void deleteByUserIdAndBoardId(@Param("userid") Long userid,@Param("boardid") Long boardid);
	
	@Query(nativeQuery = true, value = "SELECT * FROM boardliked WHERE boardId = :boardId and userId = :userId")
	List<BoardLiked> duplicateCheck(@Param("userId") Long userid, @Param("boardId") Long boardid);
}