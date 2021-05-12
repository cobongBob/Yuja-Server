package com.cobong.yuja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.ReportedBoards;

public interface ReportedRepository extends JpaRepository<ReportedBoards, Long> {

}
