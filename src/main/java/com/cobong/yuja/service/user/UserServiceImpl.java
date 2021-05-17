package com.cobong.yuja.service.user;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.config.auth.PrincipalDetails;
import com.cobong.yuja.config.jwt.CookieProvider;
import com.cobong.yuja.config.jwt.JwtTokenProvider;
import com.cobong.yuja.config.oauth.GoogleUser;
import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.AuthorityNames;
import com.cobong.yuja.model.ProfilePicture;
import com.cobong.yuja.model.RefreshToken;
import com.cobong.yuja.model.User;
import com.cobong.yuja.model.YoutubeConfirm;
import com.cobong.yuja.payload.request.user.LoginRequest;
import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.payload.request.user.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.user.UserForClientResponseDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;
import com.cobong.yuja.repository.RefreshTokenRepository;
import com.cobong.yuja.repository.user.AuthoritiesRepository;
import com.cobong.yuja.repository.user.ProfilePictureRepository;
import com.cobong.yuja.repository.user.UserRepository;
import com.cobong.yuja.repository.user.YoutubeConfirmRepository;
import com.google.common.io.Files;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final ProfilePictureRepository profilePictureRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthoritiesRepository authoritiesRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JavaMailSender javaMailSender;
    private final YoutubeConfirmRepository youtubeConfirmRepository;
	
	@Override
	@Transactional
	public UserResponseDto save(UserSaveRequestDto dto) {		
		if(userRepository.existsByUsername(dto.getUsername())) {
			throw new IllegalAccessError("이미 가입되어 있는 유저입니다");
		}
		
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));

		Authorities authorities = authoritiesRepository.findByAuthority(AuthorityNames.GENERAL).get();

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
		.isMarketingChecked(dto.getIsMarketingChecked())
		.youtubeUrl(dto.getYoutubeUrl())
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
		
		System.out.println("/n==============================  Visited  ==============================\n");
		System.out.println("\n dto.getYoutube:  "+ dto.getYoutubeImgId()+"            /////////////// \n");
		
		if (dto.getYoutubeImgId() != 0L) {
			System.out.println("/n==============================  Visited  ==============================\n");
			YoutubeConfirm youtubeConfirm = youtubeConfirmRepository.findById(dto.getYoutubeImgId())
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
		UserResponseDto userResponseDto = new UserResponseDto().entityToDto(user);
		return userResponseDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserResponseDto findById(Long id) {
		User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));
		UserResponseDto dto = new UserResponseDto().entityToDto(user);
		
		Optional<ProfilePicture> optProfilePicture = profilePictureRepository.findByUserUserId(id);
		if(optProfilePicture.isPresent()) {
			ProfilePicture profilePicture = optProfilePicture.get();
			dto.setProfilePic(profilePicture.getUploadPath());			
		} else {
			dto.setProfilePic("");
		} 
		return dto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserResponseDto> findAll(){
		List<UserResponseDto> users = new ArrayList<UserResponseDto>();
		List<User> entityList = userRepository.findAll();
		
		for(User user: entityList) {
			UserResponseDto dto = new UserResponseDto().entityToDto(user);
			Optional<ProfilePicture> optProfilePicture = profilePictureRepository.findByUserUserId(user.getUserId());
			if(optProfilePicture.isPresent()) {
				ProfilePicture profilePicture = optProfilePicture.get();
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
	public UserResponseDto modify(Long bno, UserUpdateRequestDto userUpdateRequestDto, Long userId) {
		User attemptingUser = userRepository.findById(userId).orElseThrow(() -> new IllegalAccessError("해당 유저가 없습니다."));
		List<Authorities> roles = attemptingUser.getAuthorities(); 
		boolean isAdminOrManager = false;
		for(Authorities auth: roles) {
			if(auth.getAuthority() == AuthorityNames.ADMIN || auth.getAuthority() == AuthorityNames.MANAGER) {
				isAdminOrManager = true;
			}
		}
		if(bno != userId && !isAdminOrManager) {
			throw new IllegalAccessError("관리자가 아니므로 해당 유저의 정보를 삭제할 수 없습니다");
		}
		
		User user = userRepository.findById(bno)
				.orElseThrow(() -> new IllegalAccessError("해당유저 없음" + bno));
		
		user.modify(userUpdateRequestDto.getUsername(), userUpdateRequestDto.getPassword(), 
				userUpdateRequestDto.getNickname(),userUpdateRequestDto.getRealName(),
				userUpdateRequestDto.getBday(),userUpdateRequestDto.getProvidedId(), 
				userUpdateRequestDto.getProvider(), userUpdateRequestDto.getAddress(), 
				userUpdateRequestDto.getPhone(), userUpdateRequestDto.getBsn(), 
				userUpdateRequestDto.getYoututubeUrl(), false);
		
		UserResponseDto dto = new UserResponseDto().entityToDto(user);

		if(userUpdateRequestDto.getProfilePicId() != 0L) {
			//updateRequest에서 프로필 사진이 변경되는떄(넘어오는 ProfilePictureId가 존재할 때)

			ProfilePicture profilePicture = profilePictureRepository.findById(userUpdateRequestDto.getProfilePicId()).orElseThrow(()-> new IllegalArgumentException("해당 프로필 사진을 찾을수 없습니다."));
			//새로 등록될 프로필사진
			
			if(profilePictureRepository.findByUserUserId(user.getUserId()) != null) {
				//만약 유저가 이전에 프로필사진을 등록시켜 놓은 경우
				
				Optional<ProfilePicture> optOriginalProfilePicture = profilePictureRepository.findByUserUserId(user.getUserId());
				//원래 존재하던 프로필사진
				if(optOriginalProfilePicture.isPresent()) {
					//원래 있던 프로필사진 삭제
					ProfilePicture originalProfilePicture = optOriginalProfilePicture.get();
					File toDel = new File(originalProfilePicture.getUploadPath());
					if(toDel.exists()) {
						toDel.delete();				
					} else {
						throw new IllegalAccessError("서버에 해당 이미지가 존재하지 않습니다");
					}
					profilePictureRepository.delete(originalProfilePicture);
				}
			}
			// 새로추가된 프로필사진 이동후 저장.
			if(!profilePicture.isFlag()) {
				try {
					File temp = new File(profilePicture.getTempPath());
					File dest = new File(profilePicture.getUploadPath());
					Files.move(temp, dest);
				} catch (IOException e) {
					e.printStackTrace();
					throw new IllegalAccessError("서버에 해당 이미지가 존재하지 않습니다");
				}
				profilePicture.completelySave();
				profilePicture.addUser(user);
			}
		}
		return dto;
	}

	@Override
	@Transactional
	public String delete(Long bno, Long userId) {
		User attemptingUser = userRepository.findById(userId).orElseThrow(() -> new IllegalAccessError("해당 유저가 없습니다."));
		List<Authorities> roles = attemptingUser.getAuthorities(); 
		boolean isAdminOrManager = false;
		for(Authorities auth: roles) {
			if(auth.getAuthority() == AuthorityNames.ADMIN || auth.getAuthority() == AuthorityNames.MANAGER) {
				isAdminOrManager = true;
			}
		}
		if(bno != userId && !isAdminOrManager) {
			throw new IllegalAccessError("관리자가 아니므로 해당 유저의 정보를 삭제할 수 없습니다");
		}
		
		Optional<ProfilePicture> optOriginalProfilePicture = profilePictureRepository.findByUserUserId(bno);
		if(optOriginalProfilePicture.isPresent()) {
			ProfilePicture originalProfilePicture = optOriginalProfilePicture.get();
			File toDel = new File(originalProfilePicture.getUploadPath());
			if(toDel.exists()) {
				toDel.delete();				
			} 
		}
		userRepository.deleteById(bno);
		return "success";
	}
	
	@Override
	@Transactional
	public void deleteUnflagged() {
		List<RefreshToken> tokensToDel = refreshTokenRepository.findAll();
		Instant now = Instant.now();
		for(RefreshToken token: tokensToDel) {
			if(token.getUpdatedDate().getNano() - now.getNano() < 0) {
				refreshTokenRepository.delete(token);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponseDto findByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));
		UserResponseDto dto = new UserResponseDto().entityToDto(user);
		return dto;
	}
	@Override
	@Transactional(readOnly = true)
	public UserForClientResponseDto findByUsernameForClient(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));
		UserForClientResponseDto dto = new UserForClientResponseDto().entityToDto(user);
		return dto;
	}
	
	@Override
	public Cookie[] signIn(LoginRequest loginRequest) {
		User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(()-> new IllegalArgumentException("이메일이나 비밀번호가 일치하지 않습니다."));
		System.out.println("/////////////////////////////////////////"+user.isBanned());
		if(user.isBanned()) {
			throw new IllegalAccessError("경고등의 이유로 이용이 정지된 아이디 입니다");
		}
		UserResponseDto dto = new UserResponseDto().entityToDto(user);
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token  = jwtTokenProvider.generateToken(authentication);
		String refreshJwt  = jwtTokenProvider.generateRefreshToken(authentication);
		Cookie accessToken = cookieProvider.createCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, token);
		Cookie refreshToken = cookieProvider.createCookie(JwtTokenProvider.REFRESH_TOKEN_NAME, refreshJwt);
		RefreshToken refreshTokenEntity = new RefreshToken(dto.getId(),refreshJwt);
		refreshTokenRepository.save(refreshTokenEntity);
		return new Cookie[] {accessToken,refreshToken};
	}
	
	@Override
	@Transactional(readOnly = true)
	public String verify(String username) {
		User user = userRepository.findByUsername(username).orElse(null);
		if(user != null) {
			throw new IllegalAccessError("이미 가입된 이메일입니다.");
		}
		String pattern = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		if(Pattern.matches(pattern, username) == false) {
			throw new IllegalAccessError("올바른 이메일 형식이 아닙니다. ");
		}

		String verifyNum = "";
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			verifyNum += (char) (random.nextInt(25) + 65); // A~Z까지 랜덤 알파벳 생성
			verifyNum += random.nextInt(10);
		}
		StringBuffer content = new StringBuffer();
		content.append("<!DOCTYPE html>");
		content.append("<html>");
		content.append("<head>");
		content.append("</head>");
		content.append("<body>");
		content.append("<div style=\"width: 400px; height: 300px; border: 4px solid #ff9411; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">");
		content.append("<div style=\"margin: 0; padding: 0 5px; font-size: 25px; font-weight: 400;\">");
		content.append("<img width='77' src='cid:logo'>");
		content.append("<span style=\"color: #ff9411;font-weight:bold; font-size: 44px;\">유</span>튜버와 <span style=\"color: #ff9411;font-weight:bold; font-size: 44px;\">자</span>유롭게</span><br />");
		content.append("<span style=\"font-size: 17px;\">인증번호를 정확히 입력해주세요.</span></div>\n");
		content.append("<p style=\"font-size: 15px; line-height: 25px; margin-top: 45px;text-align:center\"> 인증번호 : <span style=\"font-weight:bold\">" );
		content.append(verifyNum);
		content.append("</span></p></div>");
		content.append("</body>");
		content.append("</html>");
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true, "utf-8");
			message.setTo(username);
			message.setSubject("유자 회원가입 인증번호 메일입니다.");
			message.setText(content.toString(),true);
			message.addInline("logo", new ClassPathResource("/static/imgs/yuzu05.png"));
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new IllegalAccessError("서버 오류로 인한 메일 발송 실패");
		}
		javaMailSender.send(mimeMessage);
		return verifyNum;
	}

	@Override
	@Transactional
	public Cookie[] signOut() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails principalDetails = null;
		if(authentication.getPrincipal() instanceof PrincipalDetails) {
			principalDetails = (PrincipalDetails) authentication.getPrincipal();
		} else {
			Cookie accessToken = cookieProvider.logOutCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, null);
			Cookie refreshToken = cookieProvider.logOutCookie(JwtTokenProvider.REFRESH_TOKEN_NAME, null);
			return new Cookie[] {accessToken,refreshToken};
		}
		String token  = jwtTokenProvider.generateToken(authentication);
		String refreshJwt  = jwtTokenProvider.generateRefreshToken(authentication);
		Cookie accessToken = cookieProvider.logOutCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, token);
		Cookie refreshToken = cookieProvider.logOutCookie(JwtTokenProvider.REFRESH_TOKEN_NAME, refreshJwt);
		refreshTokenRepository.deleteByUserId(principalDetails.getUserId());
		return new Cookie[] {accessToken,refreshToken};
	}

	@Override
	@Transactional(readOnly = true)
	public String checkemail(String username) {
		String pattern = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		if(username == null || username == "") {
			return "";
		}else if(Pattern.matches(pattern, username) == false) {
			return "사용 불가능한 이메일 형식 입니다.";
		}
		User user = userRepository.findByUsername(username).orElse(null);
		if(user == null) {
			return "";
		} else {
			return "이미 가입 된 이메일 입니다.";
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public String checkNickname(String username) {
		String pattern = "^[a-zA-Z0-9가-힣ㄱ-ㅎ ]{2,20}$";
		if(username == null || username == "") {
			return "";
		}else if(Pattern.matches(pattern, username) == false) {
			return "닉네임은 2자 이상의 영문, 한글, 숫자만 사용 가능합니다.";
		}
		if(userRepository.existsByNickname(username)) {
			return "사용 중인 닉네임 입니다.";
		}
		return "";
	}
	
	@Override
	@Transactional
	public GoogleUser googleOauthCheck(Map<String, Object> data) {
		
		Map<String, Object> profile = (Map<String, Object>) data.get("profileObj");
		String username = (String) profile.get("email");
		Boolean user = userRepository.existsByUsername(username);
		GoogleUser googleUser = new GoogleUser();
		googleUser.setPassword("코봉밥");
		
//		System.out.println("username 존재여부 : "+ user);
		// 201 -> 회원가입
		if (user.equals(false)) {
			googleUser.setFlag(true);
		}
		googleUser.setAttribute(profile);
		return googleUser;
	}

	@Override
	@Transactional
	public String resetPassword(String username) {
		User user = userRepository.findByUsername(username).orElse(null);
		System.out.println(user);
		if(user == null) {
			return "존재하지 않는 회원입니다.";
		} else {
			String pattern = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
			if(Pattern.matches(pattern, username) == false) {
				return "올바른 이메일 형식이 아닙니다. ";
			}
			String tempPassword = "";
			Random random = new Random();
			for (int i = 0; i < 4; i++) {
				tempPassword += (char) (random.nextInt(25) + 97); // a~z까지 랜덤 알파벳 생성
				tempPassword += random.nextInt(10);
			}
			StringBuffer content = new StringBuffer();
			content.append("<!DOCTYPE html>");
			content.append("<html>");
			content.append("<head>");
			content.append("</head>");
			content.append("<body>");
			content.append("<div style=\"width: 400px; height: 300px; border: 4px solid #ff9411; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">");
			content.append("<div style=\"margin: 0; padding: 0 5px; font-size: 25px; font-weight: 400;\">");
			content.append("<img width='77' src='cid:logo'>");
			content.append("<span style=\"color: #ff9411;font-weight:bold; font-size: 44px;\">유</span>튜버와 <span style=\"color: #ff9411;font-weight:bold; font-size: 44px;\">자</span>유롭게</span><br />");
			content.append("<span style=\"font-size: 17px;\">임시 비밀번호입니다. 로그인 후 비밀번호를 바꿔주세요.</span></div>\n");
			content.append("<p style=\"font-size: 15px; line-height: 25px; margin-top: 45px;text-align:center\"> 임시 비밀번호 : <span style=\"font-weight:bold\">" );
			content.append(tempPassword);
			content.append("</span></p></div>");
			content.append("</body>");
			content.append("</html>");
			
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			try {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true, "utf-8");
				message.setTo(username);
				message.setSubject("유자 임시 비밀번호 메일입니다.");
				message.setText(content.toString(),true);
				message.addInline("logo", new ClassPathResource("/static/imgs/yuzu05.png"));
			} catch (MessagingException e) {
				e.printStackTrace();
				throw new IllegalAccessError("서버 오류로 인한 메일 발송 실패");
			}
			javaMailSender.send(mimeMessage);
			user.resetPasword(passwordEncoder.encode(tempPassword));
		}
		return "임시비밀번호 메일 발송완료";
	}

	@Override
	@Transactional
	public UserResponseDto banned(Long bno, UserUpdateRequestDto userUpdateRequestDto) {
		User user = userRepository.findById(bno)
				.orElseThrow(()-> new IllegalAccessError("해당유저 없음 " +bno));
		
		user.modify(userUpdateRequestDto.getUsername(), userUpdateRequestDto.getPassword(), 
				userUpdateRequestDto.getNickname(),userUpdateRequestDto.getRealName(),
				userUpdateRequestDto.getBday(),userUpdateRequestDto.getProvidedId(), 
				userUpdateRequestDto.getProvider(), userUpdateRequestDto.getAddress(), 
				userUpdateRequestDto.getPhone(),userUpdateRequestDto.getBsn(), 
				userUpdateRequestDto.getYoututubeUrl(), true);
		
		UserResponseDto dto = new UserResponseDto().entityToDto(user);
		return dto;
	}
}
