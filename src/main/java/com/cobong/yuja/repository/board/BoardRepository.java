package com.cobong.yuja.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, CustomBoardRepository{

}
