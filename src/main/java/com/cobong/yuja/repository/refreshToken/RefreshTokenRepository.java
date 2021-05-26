package com.cobong.yuja.repository.refreshToken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);
    Optional<RefreshToken> deleteByUserId(Long userId);
}
