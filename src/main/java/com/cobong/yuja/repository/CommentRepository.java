package com.cobong.yuja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.BoardComment;

public interface CommentRepository extends JpaRepository<BoardComment, Long>{

}
