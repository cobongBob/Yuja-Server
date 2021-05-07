package com.cobong.yuja.service.user;

import java.util.List;

import javax.servlet.http.Cookie;

import com.cobong.yuja.payload.request.user.LoginRequest;
import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.payload.request.user.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;

public interface UserService {

	UserResponseDto save(UserSaveRequestDto dto);

	UserResponseDto findById(Long id);

	List<UserResponseDto> findAll();

	UserResponseDto modify(Long bno, UserUpdateRequestDto userUpdateRequestDto);

	String delete(Long bno);

	UserResponseDto findByUsername(String username);
	
	Cookie[] signIn(LoginRequest loginRequest);

}