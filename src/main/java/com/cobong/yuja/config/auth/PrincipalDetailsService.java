package com.cobong.yuja.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.User;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;

	
	@Transactional
	public PrincipalDetails loadUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(()-> new UsernameNotFoundException("not found with userId : " + userId));
		
		return PrincipalDetails.create(user);
	}

	// 로그인 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 User user = userRepository.findByUsername(username)
				 .orElseThrow(()-> new UsernameNotFoundException("not found with username " + username ));
		 
		return PrincipalDetails.create(user);
	}

}
