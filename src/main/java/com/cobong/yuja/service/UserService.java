package com.cobong.yuja.service;

import java.util.List;

import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.UserSaveRequestDto;
import com.cobong.yuja.payload.request.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.UserResponseDto;

public interface UserService {

	User save(UserSaveRequestDto dto);

	UserResponseDto findById(Long id);

	List<UserResponseDto> findAll();

	UserResponseDto modify(Long bno, UserUpdateRequestDto userUpdateRequestDto);

	String delete(Long bno);

}