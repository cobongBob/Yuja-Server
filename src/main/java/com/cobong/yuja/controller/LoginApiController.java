package com.cobong.yuja.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.config.jwt.CookieProvider;
import com.cobong.yuja.config.jwt.JwtTokenProvider;
import com.cobong.yuja.model.RefreshToken;
import com.cobong.yuja.payload.request.user.LoginRequest;
import com.cobong.yuja.payload.response.user.UserResponseDto;
import com.cobong.yuja.repository.RefreshTokenRepository;
import com.cobong.yuja.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth") // 설정해야댐
@RequiredArgsConstructor
public class LoginApiController {
	
	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider jwtTokenProvider;
	
	private final CookieProvider cookieProvider;
	
    private final RefreshTokenRepository refreshTokenRepository;
    
    private final UserService userService;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,HttpServletResponse res) {
		UserResponseDto dto = userService.findByUsername(loginRequest.getUsername());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token  = jwtTokenProvider.generateToken(authentication);
		String refreshJwt  = jwtTokenProvider.generateRefreshToken(authentication);
		Cookie accessToken = cookieProvider.createCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, token);
		Cookie refreshToken = cookieProvider.createCookie(JwtTokenProvider.REFRESH_TOKEN_NAME, refreshJwt);
		RefreshToken refreshTokenEntity = new RefreshToken(dto.getId(),refreshJwt);
		refreshTokenRepository.save(refreshTokenEntity);
		res.addCookie(accessToken);
		res.addCookie(refreshToken);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}
}
