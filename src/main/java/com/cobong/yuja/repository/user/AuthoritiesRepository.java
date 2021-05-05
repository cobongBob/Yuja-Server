package com.cobong.yuja.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.AuthorityNames;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {
	Optional<Authorities> findByAuthority(AuthorityNames authorityNames);
}
