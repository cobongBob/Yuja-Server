package com.cobong.yuja.controller;

import java.net.URI;
import java.util.Collections;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cobong.yuja.config.jwt.JwtAuthenticationResponse;
import com.cobong.yuja.config.jwt.JwtTokenProvider;
import com.cobong.yuja.exception.AppException;
import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.AuthorityNames;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.user.LoginRequest;
import com.cobong.yuja.payload.request.user.SignUpRequest;
import com.cobong.yuja.payload.response.user.ApiResponse;
import com.cobong.yuja.repository.user.AuthoritiesRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth") // 설정해야댐
@RequiredArgsConstructor
public class AuthController {

	// filter에서 유저내용을 UsernamePasswordToken으로 담은게 여기에 이씀
	// 이거 사용할려고 config에서 빈 인젝트
	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider jwtTokenProvider;
	
	private final UserRepository userRepository;

	private final AuthoritiesRepository authoritiesRepository;

	private final PasswordEncoder passwordEncoder;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtTokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}
	
	// 서비스, 컨트롤러 옮기기 낼하자잉
//	@PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
//        if(UserRepository.existsByUsername(signUpRequest.getUsername())) {
//            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
//                    HttpStatus.BAD_REQUEST);
//        }
//
//
//        // Creating user's account
//        User user = new User(signUpRequest.getRealname(), signUpRequest.getUsername(),
//                signUpRequest.getAddress(), signUpRequest.getBday(), signUpRequest.getBsn(),
//                signUpRequest.getPassword(), signUpRequest.getProfilePic(), signUpRequest.getYoutubeImg());
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        Authorities authorities = authoritiesRepository.findByAuthority(AuthorityNames.GENERAL)
//                .orElseThrow(() -> new AppException("User Role not set."));
//
//        //?
//        user.setRoles(Collections.singleton(userRole));
//
//        User result = userRepository.save(user);
//
//        // ?
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/api/users/{username}")
//                .buildAndExpand(result.getUsername()).toUri();
//
//        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
//    }
}
