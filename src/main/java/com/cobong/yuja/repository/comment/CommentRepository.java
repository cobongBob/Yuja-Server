package com.cobong.yuja.repository.comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cobong.yuja.model.BoardComment;

public interface CommentRepository extends JpaRepository<BoardComment, Long>, CustomCommentRepository{

	@Query("select c from BoardComment c left join fetch c.parent where c.commentId = :commentId")
	Optional<BoardComment> findCommentByIdWithParent(@Param("commentId") Long id);
	
	//select * from boardComment where parentId = boardComment.commentId(해당댓글)
	@Query("select c from BoardComment c where c.parent.commentId = :parentId")
	Optional<List<BoardComment>> findByIdParentId(@Param("parentId") Long id);
}
