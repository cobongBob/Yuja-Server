package com.cobong.yuja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
