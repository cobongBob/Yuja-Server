package com.cobong.yuja.repository.comment;

import java.util.List;

import com.cobong.yuja.model.BoardComment;

public interface CustomCommentRepository {
	List<BoardComment> findCommentByBoardId(Long boardId);
}
