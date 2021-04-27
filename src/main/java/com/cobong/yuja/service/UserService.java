package com.cobong.yuja.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.UserSaveRequestDto;
import com.cobong.yuja.payload.response.UserResponseDto;
import com.cobong.yuja.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	
	@Transactional
	public User userSave(UserSaveRequestDto dto) {
		return userRepository.save(dto.dtoToEntity());
	}

	/*
	@Transactional(readOnly = true)
	public List<User> findAllUser(){
		return userRepository.findAll().stream().map(null);
	}	  
	  */
	public UserResponseDto findById(Long id) {
		User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));
		UserResponseDto dto = new UserResponseDto().entityToDto(user);
		return dto;
	}
	
//	@Transactional(readOnly = true)
//	public List<UserResponseDto> findAll(){
//		return userRepository.findAll().stream().map(UserListResponseDto::new)
//                .collect(Collectors.toList());
//	}
}
