package com.cobong.yuja.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.repository.user.AuthoritiesRepository;
import com.cobong.yuja.repository.user.UserRepository;

@RestController
@RequestMapping("/api/auth") // 설정해야댐
public class AuthController {
	
	// filter에서 유저내용을 UsernamePasswordToken으로 담은게 여기에 이씀
	// 이거 사용할려고 config에서 빈 인젝트
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthoritiesRepository authoritiesRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;

}
