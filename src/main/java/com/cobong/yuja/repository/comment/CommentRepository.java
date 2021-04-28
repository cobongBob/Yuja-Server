package com.cobong.yuja.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.BoardComment;

public interface CommentRepository extends JpaRepository<BoardComment, Long>, CustomCommentRepository{

}
