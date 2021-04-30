package com.cobong.yuja.repository.attach;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cobong.yuja.model.BoardAttach;

public interface AttachRepository extends JpaRepository<BoardAttach, Long>, CustomAttachRepository {

//  각 게시판마다 첨부파일 목록 가져오기 
//	@Query("SELECT a FROM Boardattach a WHERE boardId = :{id}")
//	List<BoardAttach> findAllByBoardId(@Param("id") Long id);	
}
