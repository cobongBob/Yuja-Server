package com.cobong.yuja.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.config.jwt.JwtAuthenticationResponse;
import com.cobong.yuja.config.jwt.JwtTokenProvider;
import com.cobong.yuja.payload.request.user.LoginRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth") // 설정해야댐
@RequiredArgsConstructor
public class LoginApiController {
	
	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider jwtTokenProvider;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtTokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}
}
