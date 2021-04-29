package com.cobong.yuja.repository.comment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cobong.yuja.model.BoardComment;

public interface CommentRepository extends JpaRepository<BoardComment, Long>, CustomCommentRepository{

	@Query("select c from BoardComment c left join fetch c.parent where c.commentId = :commentId")
	Optional<BoardComment> findCommentByIdWithParent(@Param("commentId") Long id);
}
