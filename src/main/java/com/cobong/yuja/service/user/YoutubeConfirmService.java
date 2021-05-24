package com.cobong.yuja.service.user;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.AuthorityNames;
import com.cobong.yuja.model.User;
import com.cobong.yuja.model.YoutubeConfirm;
import com.cobong.yuja.payload.request.user.YoutubeConfirmFIleSaveDto;
import com.cobong.yuja.payload.request.user.YoutubeConfirmRequestDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;
import com.cobong.yuja.payload.response.user.YoutubeConfirmResponseDto;
import com.cobong.yuja.repository.user.AuthoritiesRepository;
import com.cobong.yuja.repository.user.UserRepository;
import com.cobong.yuja.repository.user.YoutubeConfirmRepository;
import com.google.common.io.Files;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class YoutubeConfirmService {
	private final YoutubeConfirmRepository youtubeConfirmRepository;
	private final UserRepository userRepository;
	private final AuthoritiesRepository authoritiesRepository;
	
	private final List<String> availableTypes = Arrays.asList(".jpg",".jpeg",".png",".gif");
	
	@Transactional
	public YoutubeConfirmFIleSaveDto saveFile(MultipartFile file) {
		YoutubeConfirmFIleSaveDto dto = new YoutubeConfirmFIleSaveDto();
		try {
			String origFilename = file.getOriginalFilename();
			String fileType = origFilename.substring(origFilename.lastIndexOf("."));
			String dateNow = new SimpleDateFormat("YYYYMMddHHmmssSSSSSS").format(new Date());
			String filename = dateNow + fileType;
			
			if(!availableTypes.contains(fileType)) {
				/***
				 * 파일 형식이 ".jpg",".jpeg",".png",".gif" 중 하나가 아닐시 예외처리 필요
				 */
				throw new IllegalAccessError("파일형식은 jpg, jpeg, png, gif 중 하나여야 합니다!");
			};
			
			// 실행되는 위치의 'temp' 폴더에 파일이 저장
			String savePath = System.getProperty("user.dir") + File.separator+"files" + File.separator +"temp";
			
			
			// 파일이 저장되는 폴더가 없으면 폴더를 생성
			if (!new File(savePath).exists()) {
				try {
					new File(savePath).mkdirs(); //mkdirs는 폴더안에 폴더를 찾는데 그 상위폴더 조차 존재치 않으면 만들어준다.
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			savePath += File.separator + filename;

			file.transferTo(new File(savePath));
			
	        String uploadPath = System.getProperty("user.dir") + File.separator+"files" + File.separator + "youtubeConfirm";
			
			if (!new File(uploadPath).exists()) {
				try {
					new File(uploadPath).mkdirs();
					System.out.println(uploadPath);
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			uploadPath += File.separator + filename;
			
			dto.setOrigFilename(origFilename);
			dto.setUploadPath(uploadPath);
			dto.setFileName(filename);
			dto.setTempPath(savePath);
			
			YoutubeConfirm youtubeConfirm = youtubeConfirmRepository.save(dto.toEntitiy());
			dto.setYoutubeConfirmId(youtubeConfirm.getYoutubeConfirmId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@Transactional
	public void deleteUnflagged() {
		List<YoutubeConfirm> youtubeConfirmsToDel = youtubeConfirmRepository.findAllByFlag();
		
		for(YoutubeConfirm youtubeConfirm: youtubeConfirmsToDel) {
			youtubeConfirmRepository.delete(youtubeConfirm);
		}
	}

	@Transactional
	public YoutubeConfirmRequestDto apply(YoutubeConfirmRequestDto dto) {
		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalAccessError("존재하지 않는 유저입니다"));
		user.modify(user.getUsername(), user.getNickname(), user.getRealName(), 
				user.getBday(), user.getProvidedId(), user.getProvider(), user.getAddress(), 
				user.getPhone(), dto.getBsn(), dto.getYoutubeUrl(), false);
		userRepository.save(user);
		
		if (dto.getYoutubeConfirmId() != 0) {
			YoutubeConfirm youtubeConfirm = youtubeConfirmRepository.findById(dto.getYoutubeConfirmId())
					.orElseThrow(() -> new IllegalArgumentException("해당 유튜브 인증 이미지를 찾을수 없습니다."));
			if (!youtubeConfirm.isFlag()) {
				File temp = new File(youtubeConfirm.getTempPath());
				File dest = new File(youtubeConfirm.getUploadPath());
				try {
					Files.move(temp, dest);
				} catch (IOException e) {
					e.printStackTrace();
				}
				youtubeConfirm.completelySave();
				youtubeConfirm.addUser(user);
			}
		}
		return dto;
	}
	
	@Transactional
	public List<YoutubeConfirmResponseDto> findUnauthorized(){
		List<YoutubeConfirmResponseDto> dtos = new ArrayList<YoutubeConfirmResponseDto>();
		List<YoutubeConfirm> youtubeConfirms = youtubeConfirmRepository.findAllByAuthorized();
		
		for(YoutubeConfirm youtubeConfirm : youtubeConfirms) {
			YoutubeConfirmResponseDto dto = new YoutubeConfirmResponseDto().entityToDto(youtubeConfirm);
			if(youtubeConfirm.getUser() != null) {
				User user = youtubeConfirm.getUser();
				UserResponseDto userDto = new UserResponseDto().entityToDto(user);
				dto.setUser(userDto);
				dtos.add(dto);				
			}
		}
		return dtos;
	}
	
	@Transactional
	public YoutubeConfirmResponseDto findById(Long youtubeConfirmId) {
		YoutubeConfirm youtubeConfirm = youtubeConfirmRepository.findById(youtubeConfirmId)
				.orElseThrow(() -> new IllegalAccessError("해당 유저의 요청이 존재하지 않습니다."));
		
		YoutubeConfirmResponseDto dto = new YoutubeConfirmResponseDto().entityToDto(youtubeConfirm);
		return dto;
	}
	
	@Transactional
	public YoutubeConfirmResponseDto confirm(YoutubeConfirmRequestDto dto) {
		YoutubeConfirmResponseDto dtoToSend = new YoutubeConfirmResponseDto();
		
		YoutubeConfirm youtubeConfirm = youtubeConfirmRepository.findById(dto.getYoutubeConfirmId())
				.orElseThrow(() -> new IllegalAccessError("해당 유저의 유튜버 승격 요청이 존재하지 않습니다."));
		
		youtubeConfirm.authorizeAsYoutuber();
		
		Authorities authorities = authoritiesRepository.findByAuthority(AuthorityNames.YOUTUBER).get();
		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalAccessError("해당 유저가 존재하지 않습니다."));

		user.getAuthorities().add(authorities);
		/***
		 * 유저 엔티티에서 List<Authorities>를 가져와서 그 리스트에 추가만 하는 방식. 조인 테이블이라 가능하지 않을까..?
		 * 테스트 후 확인 바람!!
		 */
		userRepository.save(user);
		
		youtubeConfirmRepository.save(youtubeConfirm);
		dtoToSend.setYoutubeConfirmId(dto.getYoutubeConfirmId());
		dtoToSend.setYoutubeConfirmImg(youtubeConfirm.getFileName());
		return dtoToSend;
	}
	
	@Transactional
	public String rejectUser(Long youtubeConfirmId) {
		YoutubeConfirm youtubeConfirm = youtubeConfirmRepository.findById(youtubeConfirmId)
				.orElseThrow(() -> new IllegalAccessError("해당 유저의 유튜버 승격 요청이 존재하지 않습니다."));
		File youtubeImgToDel = new File(youtubeConfirm.getUploadPath());
		if(youtubeImgToDel.exists()) {
			youtubeImgToDel.delete();
		}
		youtubeConfirmRepository.deleteById(youtubeConfirmId);
		return "Success";
	}
}
