package com.cobong.yuja.service.user;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.Notification;
import com.cobong.yuja.model.User;
import com.cobong.yuja.model.YoutubeConfirm;
import com.cobong.yuja.model.enums.AuthorityNames;
import com.cobong.yuja.payload.request.user.YoutubeConfirmFIleSaveDto;
import com.cobong.yuja.payload.request.user.YoutubeConfirmRequestDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;
import com.cobong.yuja.payload.response.user.YoutubeConfirmResponseDto;
import com.cobong.yuja.repository.notification.NotificationRepository;
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
	private final NotificationRepository notificationRepository;
	
	private final List<String> availableTypes = Arrays.asList(".jfif",".pjpeg",".pjp",".jpg",".jpeg",".png",".gif");
	
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
				 * ?????? ????????? ".jpg",".jpeg",".png",".gif" ??? ????????? ????????? ???????????? ??????
				 */
				throw new IllegalAccessError("??????????????? jpg, jpeg, png, gif ??? ???????????? ?????????!");
			};
			
			// ???????????? ????????? 'temp' ????????? ????????? ??????
			String savePath = System.getProperty("user.dir") + File.separator+"files" + File.separator +"temp";
			
			
			// ????????? ???????????? ????????? ????????? ????????? ??????
			if (!new File(savePath).exists()) {
				try {
					new File(savePath).mkdirs(); //mkdirs??? ???????????? ????????? ????????? ??? ???????????? ?????? ????????? ????????? ???????????????.
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
		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalAccessError("???????????? ?????? ???????????????"));
		user.modify(user.getUsername(), user.getNickname(), user.getRealName(), 
				user.getBday(), user.getProvidedId(), user.getProvider(), user.getAddress(), 
				user.getPhone(), dto.getBsn(), dto.getYoutubeUrl(), false);
		userRepository.save(user);
		
		Optional<YoutubeConfirm> confirm = youtubeConfirmRepository.findByUserUserId(user.getUserId());
		if(confirm.isPresent()) {
			throw new IllegalAccessError("?????? ????????? ????????? ???????????????.");
		}
		
		if (dto.getYoutubeConfirmId() != 0) {
			YoutubeConfirm youtubeConfirm = youtubeConfirmRepository.findById(dto.getYoutubeConfirmId())
					.orElseThrow(() -> new IllegalArgumentException("?????? ????????? ?????? ???????????? ????????? ????????????."));
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
				.orElseThrow(() -> new IllegalAccessError("?????? ????????? ????????? ???????????? ????????????."));
		
		YoutubeConfirmResponseDto dto = new YoutubeConfirmResponseDto().entityToDto(youtubeConfirm);
		return dto;
	}
	
	@Transactional
	public YoutubeConfirmResponseDto confirm(YoutubeConfirmRequestDto dto) {
		
		//????????? ????????? ????????????
		User admin = userRepository.findById(1L).orElse(null);
		//????????? ????????? ????????????
		
		YoutubeConfirmResponseDto dtoToSend = new YoutubeConfirmResponseDto();
		
		YoutubeConfirm youtubeConfirm = youtubeConfirmRepository.findById(dto.getYoutubeConfirmId())
				.orElseThrow(() -> new IllegalAccessError("?????? ????????? ????????? ?????? ????????? ???????????? ????????????."));
		
		youtubeConfirm.authorizeAsYoutuber();
		
		Authorities authorities = authoritiesRepository.findByAuthority(AuthorityNames.YOUTUBER).get();
		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalAccessError("?????? ????????? ???????????? ????????????."));

		user.getAuthorities().add(authorities);
		/***
		 * ?????? ??????????????? List<Authorities>??? ???????????? ??? ???????????? ????????? ?????? ??????. ?????? ??????????????? ???????????? ?????????..?
		 * ????????? ??? ?????? ??????!!
		 */
		userRepository.save(user);
		
		youtubeConfirmRepository.save(youtubeConfirm);
		dtoToSend.setYoutubeConfirmId(dto.getYoutubeConfirmId());
		dtoToSend.setYoutubeConfirmImg(youtubeConfirm.getFileName());
		
		
		// ????????? ?????? ?????? ??????
		String type = "youtubeNoti"; 
		Notification notification = new Notification().createNotification(
				null, 
				admin, // sender default admin
				userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalAccessError("?????? ????????? ???????????? ????????????.")),
				type,
				null);
		notificationRepository.save(notification);	
		return dtoToSend;
	}
	
	@Transactional
	public String rejectUser(Long youtubeConfirmId) {
		//????????? ????????? ????????????
		User admin = userRepository.findById(1L).orElse(null);
		//????????? ????????? ????????????
		YoutubeConfirm youtubeConfirm = youtubeConfirmRepository.findById(youtubeConfirmId)
				.orElseThrow(() -> new IllegalAccessError("?????? ????????? ????????? ?????? ????????? ???????????? ????????????."));
		File youtubeImgToDel = new File(youtubeConfirm.getUploadPath());
		if(youtubeImgToDel.exists()) {
			youtubeImgToDel.delete();
		}
		
		// ?????? ?????? ??????
		String type = "rejectNoti"; 
		Notification notification = new Notification().createNotification(
				null, 
				admin, // sender default admin
				youtubeConfirm.getUser(),
				type,
				null);
		notificationRepository.save(notification);	
		
		youtubeConfirmRepository.deleteById(youtubeConfirmId);
		return "Success";
	}
}
