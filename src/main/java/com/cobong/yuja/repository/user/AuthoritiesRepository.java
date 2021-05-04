package com.cobong.yuja.repository.user;

import java.util.Optional;

import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.AuthorityNames;

public interface AuthoritiesRepository {
	Optional<Authorities> findByAuthority(AuthorityNames authorityNames);
}
