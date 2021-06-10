package com.cobong.yuja.service.user.impl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Value;
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
import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.ChatRoomJoin;
import com.cobong.yuja.model.ProfilePicture;
import com.cobong.yuja.model.RefreshToken;
import com.cobong.yuja.model.User;
import com.cobong.yuja.model.VisitorTracker;
import com.cobong.yuja.model.YoutubeConfirm;
import com.cobong.yuja.model.enums.AuthorityNames;
import com.cobong.yuja.payload.request.user.LoginRequest;
import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.payload.request.user.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.statistics.StatisticsDto;
import com.cobong.yuja.payload.response.user.UserForClientResponseDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;
import com.cobong.yuja.repository.attach.ProfilePictureRepository;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.chat.ChatRoomJoinRepository;
import com.cobong.yuja.repository.chat.ChatRoomRepository;
import com.cobong.yuja.repository.refreshToken.RefreshTokenRepository;
import com.cobong.yuja.repository.user.AuthoritiesRepository;
import com.cobong.yuja.repository.user.UserRepository;
import com.cobong.yuja.repository.user.YoutubeConfirmRepository;
import com.cobong.yuja.repository.visitorTracker.VisitorTrackerRepository;
import com.cobong.yuja.service.user.UserService;
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
    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final VisitorTrackerRepository visitorTrackerRepository;
    private final BoardRepository boardRepository;
    @Value("${app.oauthSecret}")
	private String oauthSecret;
    
	@Override
	@Transactional
	public UserResponseDto save(UserSaveRequestDto dto) {		
		if(userRepository.existsByUsername(dto.getUsername())) {
			throw new IllegalAccessError("이미 가입되어 있는 유저입니다");
		}
		
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));

		Authorities authorities = authoritiesRepository.findByAuthority(AuthorityNames.GENERAL).get();

		String wholeAddr = dto.getAddress() + " # " + dto.getDetailAddress();
		
		User entity = User.builder()
		.username(dto.getUsername())
		.password(dto.getPassword())
		.nickname(dto.getNickname())
		.realName(dto.getRealName())
		.authorities(Collections.singletonList((authorities)))
		.bday(dto.getBday())
		.providedId(dto.getProvidedId())
		.address(wholeAddr)
		.provider(dto.getProvider())
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
		
		if (dto.getYoutubeConfirmId() != 0L) {
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
		UserResponseDto userResponseDto = new UserResponseDto().entityToDto(user);
		return userResponseDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserResponseDto findById(Long id, Long attemptingUser) {
		User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));
		
		User attempting = userRepository.findById(attemptingUser).orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));
		if(id != attemptingUser && (!attempting.getAuthorities().contains(authoritiesRepository.findByAuthority(AuthorityNames.ADMIN).get()) && !attempting.getAuthorities().contains(authoritiesRepository.findByAuthority(AuthorityNames.MANAGER).get()))) {
			throw new IllegalAccessError("회원 정보는 본인 혹은 관리자만 가능합니다.");
		}
		if(user.isDeleted()) {
			new IllegalArgumentException("해당 유저는 탈퇴한 회원입니다.");
		}
		
		UserResponseDto dto = new UserResponseDto().entityToDto(user);
		if(user.getAddress() != null) {
			dto.setAddress(user.getAddress().substring(0,user.getAddress().indexOf(" # ")));
			if(user.getAddress().contains("#")) {
				dto.setDetailAddress(user.getAddress().substring(user.getAddress().indexOf(" # ")+3));				
			}
		}
		if(youtubeConfirmRepository.findByUserUserId(id).isPresent()) {
			dto.setYoutubeConfirmImg(youtubeConfirmRepository.findByUserUserId(id).get().getFileName());
		} else {
			dto.setYoutubeConfirmImg("");
		}
		Optional<ProfilePicture> optProfilePicture = profilePictureRepository.findByUserUserId(id);
		if(optProfilePicture.isPresent()) {
			ProfilePicture profilePicture = optProfilePicture.get();
			dto.setProfilePic(profilePicture.getFileName());			
			dto.setProfilePicId(profilePicture.getProfilePicId());
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
			if(user.getAddress() != null) {
				dto.setAddress(user.getAddress().substring(0,user.getAddress().indexOf(" # ")));
				if(user.getAddress().contains("#")) {
					dto.setDetailAddress(user.getAddress().substring(user.getAddress().indexOf(" # ")+3));				
				}
			}
			if(optProfilePicture.isPresent()) {
				ProfilePicture profilePicture = optProfilePicture.get();
				dto.setProfilePic(profilePicture.getFileName());			
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
		String wholeAddr = userUpdateRequestDto.getAddress() +" # "+ userUpdateRequestDto.getDetailAddress();
		
		User user = userRepository.findById(bno)
				.orElseThrow(() -> new IllegalAccessError("해당유저 없음" + bno));
		
		user.modify(userUpdateRequestDto.getUsername(),  
				userUpdateRequestDto.getNickname(),userUpdateRequestDto.getRealName(),
				userUpdateRequestDto.getBday(),userUpdateRequestDto.getProvidedId(), 
				userUpdateRequestDto.getProvider(), wholeAddr, 
				userUpdateRequestDto.getPhone(), userUpdateRequestDto.getBsn(), 
				userUpdateRequestDto.getYoutubeUrl(), false);
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
		
		if(userUpdateRequestDto.getYoutubeConfirmId() != 0L) {
			YoutubeConfirm confirm = youtubeConfirmRepository.findById(userUpdateRequestDto.getYoutubeConfirmId())
					.orElseThrow(() -> new IllegalAccessError("유튜버 인증 이미지가 존재하지 않습니다."));
			if(youtubeConfirmRepository.findByUserUserId(user.getUserId()) != null) {
				
				Optional<YoutubeConfirm> optOriginalYoutubeConfirm = youtubeConfirmRepository.findByUserUserId(user.getUserId());
				if(optOriginalYoutubeConfirm.isPresent()) {
					YoutubeConfirm originalYoutubeConfirm = optOriginalYoutubeConfirm.get();
					File toDel = new File(originalYoutubeConfirm.getUploadPath());
					if(toDel.exists()) {
						toDel.delete();				
					} else {
						throw new IllegalAccessError("서버에 해당 이미지가 존재하지 않습니다");
					}
					youtubeConfirmRepository.delete(originalYoutubeConfirm);
				}
			}
			if(!confirm.isFlag()) {
				try {
					File temp = new File(confirm.getTempPath());
					File dest = new File(confirm.getUploadPath());
					Files.move(temp, dest);
				} catch (IOException e) {
					e.printStackTrace();
					throw new IllegalAccessError("서버에 해당 이미지가 존재하지 않습니다");
				}
				confirm.completelySave();
				confirm.authorizeAsYoutuber();
				confirm.addUser(user);
			}
		}
		return dto;
	}

	@Override
	@Transactional
	public String delete(Long bno, Long userId) {
		User attemptingUser = userRepository.findById(userId).orElseThrow(() -> new IllegalAccessError("해당 유저가 없습니다."));
		User deletinguser = userRepository.findById(bno).orElseThrow(() -> new IllegalAccessError("해당 유저가 없습니다."));
		List<Authorities> roles = attemptingUser.getAuthorities(); 
		boolean isAdminOrManager = false;
		for(Authorities auth: roles) {
			if(auth.getAuthority() == AuthorityNames.ADMIN || auth.getAuthority() == AuthorityNames.MANAGER) {
				isAdminOrManager = true;
			}
		}
		List<ChatRoomJoin> joins = chatRoomJoinRepository.findByUserUserId(bno);
		for(ChatRoomJoin join: joins) {
			chatRoomRepository.deleteById(join.getChatRoom().getRoomId());
		}
		if(bno == userId || isAdminOrManager) {
			if(deletinguser.isDeleted()) {
				deletinguser.setDeleted(false);
				return "해당 유저가 복구 되었습니다.";
			} else {
				deletinguser.setDeleted(true);
				return "success";
			}
		} else {
			throw new IllegalAccessError("탈퇴 권한이 없습니다");
		}
	}
	
	@Override
	@Transactional
	public String remove(Long uno, Long userId) {
		User attemptingUser = userRepository.findById(userId).orElseThrow(() -> new IllegalAccessError("해당 유저가 없습니다."));
		List<Authorities> roles = attemptingUser.getAuthorities(); 
		boolean isAdminOrManager = false;
		for(Authorities auth: roles) {
			if(auth.getAuthority() == AuthorityNames.ADMIN || auth.getAuthority() == AuthorityNames.MANAGER) {
				isAdminOrManager = true;
			}
		}
		if(!isAdminOrManager) {
			throw new IllegalAccessError("관리자가 아니므로 해당 유저의 정보를 삭제할 수 없습니다");
		}
		
		Optional<ProfilePicture> optOriginalProfilePicture = profilePictureRepository.findByUserUserId(uno);
		if(optOriginalProfilePicture.isPresent()) {
			ProfilePicture originalProfilePicture = optOriginalProfilePicture.get();
			File toDel = new File(originalProfilePicture.getUploadPath());
			if(toDel.exists()) {
				toDel.delete();				
			}
		}
		List<ChatRoomJoin> joins = chatRoomJoinRepository.findByUserUserId(uno);
		for(ChatRoomJoin join: joins) {
			chatRoomRepository.deleteById(join.getChatRoom().getRoomId());
		}
		
		userRepository.deleteById(uno);
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
		
		if(user.getAddress() != null) {
			dto.setAddress(user.getAddress().substring(0,user.getAddress().indexOf(" # ")));
			if(user.getAddress().contains("#")) {
				dto.setDetailAddress(user.getAddress().substring(user.getAddress().indexOf(" # ")+3));				
			}
		}
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
		
		if(user.isBanned()) {
			throw new IllegalAccessError("경고등의 이유로 이용이 정지된 아이디 입니다");
		}
		
		if(user.isDeleted()) {
			throw new IllegalAccessError("삭제된 아이디 입니다.");
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
		content.append("<span style=\"color: #ff9411;font-weight:bold; font-size: 44px;\">유</span>튜브와 <span style=\"color: #ff9411;font-weight:bold; font-size: 44px;\">자</span>유롭게</span><br />");
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
		
		@SuppressWarnings("unchecked") // 지정한 형식의 개체가 포함될 것을 확신한다 하면 어노테이션으로 ignore warning 시킴
		Map<String, Object> profile = (Map<String, Object>) data.get("profileObj");
		String username = (String) profile.get("email");
		Boolean user = userRepository.existsByUsername(username);
		GoogleUser googleUser = new GoogleUser();
		googleUser.setPassword(oauthSecret);
		
		// 201 -> 회원가입
		if (user.equals(false)) {
			googleUser.setFlag(true);
		} else {
			googleUser.setFlag(false);
		}
		googleUser.setAttribute(profile);
		return googleUser;
	}

	@Override
	@Transactional
	public String resetPassword(Map<String, String> userData) {
		User user = userRepository.findByUsername(userData.get("username")).orElse(null);
		if(user == null) {
			throw new IllegalAccessError("존재하지 않는 회원입니다.");
		} else {
			String pattern = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
			if(Pattern.matches(pattern, userData.get("username")) == false) {
				throw new IllegalAccessError("올바른 이메일 형식이 아닙니다. ");
			}
			
			user.resetPasword(passwordEncoder.encode(userData.get("password")));
		}
		return "비밀번호 변경 완료";
	}
	
	@Override
	@Transactional(readOnly = true)
	public String findPassword(String username) {
		User user = userRepository.findByUsername(username).orElse(null);
		if(user == null) {
			throw new IllegalAccessError("이메일을 확인해 주세요.");
		} else if(user.isDeleted()) {
			throw new IllegalAccessError("탈퇴한 회원입니다.");
		}
		String pattern = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		if(Pattern.matches(pattern, username) == false) {
			throw new IllegalAccessError("이메일을 확인해 주세요.");
		}
		if(user.getProvider().equals("google")) {
			throw new IllegalAccessError("구글로 가입한 계정입니다.");
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
		content.append("<span style=\"color: #ff9411;font-weight:bold; font-size: 44px;\">유</span>튜브와 <span style=\"color: #ff9411;font-weight:bold; font-size: 44px;\">자</span>유롭게</span><br />");
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
			message.setSubject("유자 회원찾기 인증번호 메일입니다.");
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
	public String banned(Long uno) {
		User user = userRepository.findById(uno)
				.orElseThrow(()-> new IllegalAccessError("해당유저 없음 " +uno));
		if(user.isBanned()) {
			user.setBanned(false);
			return "해당 유저가 밴 해제 되었습니다.";
		} else {
			user.setBanned(true);
			return "해당 유저가 밴 되었습니다.";
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public StatisticsDto statsInSevenDays(Long userId) {
		Optional<User> attemptingUser = userRepository.findById(userId);
		if(!attemptingUser.isPresent()) {
			throw new IllegalAccessError("관리자 계정으로 로그인 해 주십시오");
		}
		if(!attemptingUser.get().getAuthorities().contains(authoritiesRepository.findByAuthority(AuthorityNames.ADMIN).get()) && !attemptingUser.get().getAuthorities().contains(authoritiesRepository.findByAuthority(AuthorityNames.MANAGER).get())) {
			throw new IllegalAccessError("권한이 없습니다.");
		}
		
		ZonedDateTime weekAgo = LocalDate.now().minusDays(6).atStartOfDay().atZone(ZoneId.of("Asia/Seoul"));
		List<User> usersRegistered = userRepository.usersCreatedAfter(weekAgo.toInstant());
		Long[] signedUp = new Long[7];
		Long[] visitors = new Long[7];
		Long[] boardStat = {0L,0L,0L,0L,0L,0L,0L};
		String[] last7Days = new String[7];
		List<VisitorTracker> visitorsIn7Days = visitorTrackerRepository.visitorsAfter();
		Collections.reverse(visitorsIn7Days);
		LocalDate curInst = null;
		LocalDate targetInst = null;
		for(int i = 0; i < 7; i++) {
			Long cnt = 0L;
			curInst = LocalDateTime.ofInstant(weekAgo.toInstant().plusSeconds(86400L*i), ZoneId.of("Asia/Seoul")).toLocalDate();
			for(User user: usersRegistered) {
				targetInst = LocalDateTime.ofInstant(user.getCreatedDate(), ZoneId.of("Asia/Seoul")).toLocalDate();
				if(targetInst.isEqual(curInst)) {
					cnt++;
				}
			}
			last7Days[i] = curInst.format(DateTimeFormatter.ofPattern("yyyy-MM-dd E"));
			visitors[i] = visitorsIn7Days.get(i).getVisitorsToday();
			signedUp[i] = cnt;
		}
		List<Board> allBoards = boardRepository.findAll();
		for(Board board: allBoards) {
			switch(Long.valueOf(board.getBoardType().getBoardCode()).intValue()) {
			case 1:
				boardStat[0]++;
				break;
			case 2:
				boardStat[1]++;
				break;
			case 3:
				boardStat[2]++;
				break;
			case 4:
				boardStat[3]++;
				break;
			case 5:
				boardStat[4]++;
				break;
			case 6:
				boardStat[5]++;
				break;
			case 7:
				boardStat[6]++;
				break;
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		try {
			startDate = format.parse("2021-05-31");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<VisitorTracker> allTracks = visitorTrackerRepository.findAll();
		long diff = ((System.currentTimeMillis()-startDate.getTime())/1000)/60/60/24;
		Long[] userInc = new Long[(int) (diff+1L)];
		String[] allDates = new String[(int) (diff+1L)];
		for(int i = 0; i < diff+1L;i++) {
			curInst = LocalDateTime.ofInstant(startDate.toInstant().plusSeconds(86400L*i), ZoneId.of("Asia/Seoul")).toLocalDate();
			
			userInc[i] = allTracks.get(i).getUsersToday();
			allDates[i] = curInst.format(DateTimeFormatter.ofPattern("yyyy-MM-dd E"));
		}
		
		StatisticsDto weekStat = new StatisticsDto(signedUp, visitors, boardStat, userInc, last7Days, allDates);
		return weekStat;
	}

	@Override
	@Transactional
	public void createTracker() {
		VisitorTracker vs = visitorTrackerRepository.findLastTracker().get();
		
		vs.setUsersToday(userRepository.countUsers());
		
		VisitorTracker visitorTrack = VisitorTracker.builder().visitorsToday(0L).usersToday(userRepository.countUsers()).build();
		
		visitorTrackerRepository.save(visitorTrack);
	}
}
