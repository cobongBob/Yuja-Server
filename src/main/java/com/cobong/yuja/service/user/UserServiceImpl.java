package com.cobong.yuja.service.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.exception.AppException;
import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.AuthorityNames;
import com.cobong.yuja.model.ProfilePicture;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.payload.request.user.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;
import com.cobong.yuja.repository.user.AuthoritiesRepository;
import com.cobong.yuja.repository.user.ProfilePictureRepository;
import com.cobong.yuja.repository.user.UserRepository;
import com.google.common.io.Files;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final ProfilePictureRepository profilePictureRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthoritiesRepository authoritiesRepository;
	
	
	@Override
	@Transactional
	public UserResponseDto save(UserSaveRequestDto dto) {		
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));

		Authorities authorities = authoritiesRepository.findByAuthority(AuthorityNames.GENERAL)
				.orElseThrow(() -> new AppException("Authority(GENERAL) not set"));

		User entity = User.builder()
		.username(dto.getUsername())
		.password(dto.getPassword())
		.nickname(dto.getNickname())
		.realName(dto.getRealName())
		.authorities(Collections.singletonList((authorities)))
		.bday(dto.getBday())
		.providedId(dto.getProvidedId())
		.provider(dto.getProvider())
		.address(dto.getAddress())
		.phone(dto.getPhone())
		.bsn(dto.getBsn())
		.youtubeImg(dto.getYoutubeImg())
		.userIp(dto.getUserIp())
		.isMarketingChecked(dto.getIsMarketingChecked())
		.build();

		User user = userRepository.save(entity);


		if (dto.getProfilePicId() != 0) {
			ProfilePicture profilePicture = profilePictureRepository.findById(dto.getProfilePicId())
					.orElseThrow(() -> new IllegalArgumentException("해당 프로필 사진을 찾을수 없습니다."));
			if (!profilePicture.isFlag()) {
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
		
		ProfilePicture profilePicture = profilePictureRepository.findByUserUserId(id);
		if(profilePicture != null) {
			dto.setProfilePic(profilePicture.getUploadPath());			
		} else {
			dto.setProfilePic("");
		} 
		/***
		 * 예외처리 완료시 else에 추가해야함
		 */
		return dto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserResponseDto> findAll(){
		List<UserResponseDto> users = new ArrayList<UserResponseDto>();
		List<User> entityList = userRepository.findAll();
		
		for(User user: entityList) {
			UserResponseDto dto = new UserResponseDto().entityToDto(user);
			ProfilePicture profilePicture = profilePictureRepository.findByUserUserId(user.getUserId());
			if(profilePicture != null) {
				dto.setProfilePic(profilePicture.getUploadPath());			
			} else {
				dto.setProfilePic("");
			}
			users.add(dto);
		}
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

		if(userUpdateRequestDto.getProfilePicId() != 0L) {
			//updateRequest에서 프로필 사진이 변경되는떄(넘어오는 ProfilePictureId가 존재할 때)

			ProfilePicture profilePicture = profilePictureRepository.findById(userUpdateRequestDto.getProfilePicId()).orElseThrow(()-> new IllegalArgumentException("해당 프로필 사진을 찾을수 없습니다."));
			//새로 등록될 프로필사진
			
			if(profilePictureRepository.findByUserUserId(user.getUserId()) != null) {
				//만약 유저가 이전에 프로필사진을 등록시켜 놓은 경우
				
				ProfilePicture originalProfilePicture = profilePictureRepository.findByUserUserId(user.getUserId());
				//원래 존재하던 프로필사진
				
				//원래 있던 프로필사진 삭제
				File toDel = new File(originalProfilePicture.getUploadPath());
				if(toDel.exists()) {
					toDel.delete();				
				} else {
					System.out.println("Such File does not exist!");
				}
				profilePictureRepository.delete(originalProfilePicture);
			}
			// 새로추가된 프로필사진 이동후 저장.
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
		return dto;
	}

	@Override
	@Transactional
	public String delete(Long bno) {
		ProfilePicture originalProfilePicture = profilePictureRepository.findByUserUserId(bno);
		File toDel = new File(originalProfilePicture.getUploadPath());
		if(toDel.exists()) {
			toDel.delete();				
		} else {
			System.out.println("Such File does not exist!");
		}
		userRepository.deleteById(bno);
		return "success";
	}
}
