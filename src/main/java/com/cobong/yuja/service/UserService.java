package com.cobong.yuja.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.UserSaveRequestDto;
import com.cobong.yuja.payload.request.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.UserListResponseDto;
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
	
	public UserResponseDto findById(Long id) {
		User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));
		UserResponseDto dto = new UserResponseDto().entityToDto(user);
		return dto;
	}
	
	@Transactional(readOnly = true)
	public List<UserResponseDto> findAll(){
		List<UserResponseDto> users = new ArrayList<UserResponseDto>();
		userRepository.findAll().forEach(e -> users.add(new UserResponseDto().entityToDto(e)));
		return users;
	}

	@Transactional
	public UserResponseDto modify(Long bno, UserUpdateRequestDto userUpdateRequestDto) {
		User user = userRepository.findById(bno)
				.orElseThrow(() -> new IllegalAccessError("해당유저 없음" + bno));
		
		user.modify(userUpdateRequestDto.getUsername(), userUpdateRequestDto.getPassword(), 
				userUpdateRequestDto.getNickname(),userUpdateRequestDto.getRealName(),
				userUpdateRequestDto.getBday(), userUpdateRequestDto.getProfilePic(),
				userUpdateRequestDto.getProvidedId(), userUpdateRequestDto.getProvider(),
				userUpdateRequestDto.getAddress(), userUpdateRequestDto.getPhone(),
				userUpdateRequestDto.getBsn(), userUpdateRequestDto.getYoutubeImg(),
				userUpdateRequestDto.getUserIp());
		UserResponseDto dto = new UserResponseDto().entityToDto(user);
		return dto;
	}

	public String delete(Long bno) {
		userRepository.deleteById(bno);
		return "success";
	}
}
