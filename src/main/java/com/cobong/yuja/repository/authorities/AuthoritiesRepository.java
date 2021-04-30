package com.cobong.yuja.repository.authorities;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.UserRole;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {
    Optional<Authorities> findByName(UserRole userRole);
}
