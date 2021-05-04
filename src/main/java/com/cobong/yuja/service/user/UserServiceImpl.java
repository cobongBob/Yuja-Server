package com.cobong.yuja.service.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.ProfilePicture;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.payload.request.user.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;
import com.cobong.yuja.repository.user.ProfilePictureRepository;
import com.cobong.yuja.repository.user.UserRepository;
import com.google.common.io.Files;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final ProfilePictureRepository profilePictureRepository;
	
	@Override
	@Transactional
	public UserResponseDto save(UserSaveRequestDto dto) {		
		User user = userRepository.save(dto.dtoToEntity());
		if(dto.getProfilePicId() != 0) {
			ProfilePicture profilePicture = profilePictureRepository.findById(dto.getProfilePicId()).orElseThrow(()-> new IllegalArgumentException("해당 프로필 사진을 찾을수 없습니다."));
			if(!profilePicture.isFlag()) {
				File temp = new File(profilePicture.getTempPath());
				File dest = new File(profilePicture.getUploadPath());
				try {
					Files.move(temp, dest);
				} catch (IOException e) {
					e.printStackTrace();
				}
				profilePicture.completelySave();
				profilePicture.addUser(user);
			}
		}
		UserResponseDto userResponseDto = new UserResponseDto().entityToDto(user);
		return userResponseDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserResponseDto findById(Long id) {
		User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));
		UserResponseDto dto = new UserResponseDto().entityToDto(user);
		ProfilePicture profilePicture = profilePictureRepository.findByUserIdAndFlag(id);
		/***
		 * 미완!! 여기를 끝내야 한드아아아아아아아
		 */
		dto.setProfilePic(profilePicture.getUploadPath());
		return dto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserResponseDto> findAll(){
		List<UserResponseDto> users = new ArrayList<UserResponseDto>();
		userRepository.findAll().forEach(e -> users.add(new UserResponseDto().entityToDto(e)));
		return users;
	}

	@Override
	@Transactional
	public UserResponseDto modify(Long bno, UserUpdateRequestDto userUpdateRequestDto) {
		User user = userRepository.findById(bno)
				.orElseThrow(() -> new IllegalAccessError("해당유저 없음" + bno));
		
		user.modify(userUpdateRequestDto.getUsername(), userUpdateRequestDto.getPassword(), 
				userUpdateRequestDto.getNickname(),userUpdateRequestDto.getRealName(),
				userUpdateRequestDto.getBday(),userUpdateRequestDto.getProvidedId(), 
				userUpdateRequestDto.getProvider(), userUpdateRequestDto.getUserIp(),
				userUpdateRequestDto.getAddress(), userUpdateRequestDto.getPhone(),
				userUpdateRequestDto.getBsn(), userUpdateRequestDto.getYoutubeImg());
		UserResponseDto dto = new UserResponseDto().entityToDto(user);
		
		
		
		return dto;
	}

	@Override
	@Transactional
	public String delete(Long bno) {
		userRepository.deleteById(bno);
		return "success";
	}
}
