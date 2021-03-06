package com.cobong.yuja.service.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.cobong.yuja.config.oauth.GoogleUser;
import com.cobong.yuja.config.oauth.KakaoUser;
import com.cobong.yuja.payload.request.user.LoginRequest;
import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.payload.request.user.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.statistics.StatisticsDto;
import com.cobong.yuja.payload.response.user.UserForClientResponseDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;

public interface UserService {

	UserResponseDto save(UserSaveRequestDto dto);

	UserResponseDto findById(Long id, Long attemptingUser);

	List<UserResponseDto> findAll();

	UserResponseDto modify(Long bno, UserUpdateRequestDto userUpdateRequestDto, Long userId);

	String delete(Long bno, Long userId);

	UserResponseDto findByUsername(String username);
	
	Cookie[] signIn(LoginRequest loginRequest);

	String verify(String username);
	
	void deleteUnflagged();

	Cookie[] signOut();

	String checkemail(String username);

	String checkNickname(String username);
	
	GoogleUser googleOauthCheck(Map<String, Object> data);
	
	KakaoUser kakaoOauthCheck(Map<String, Object> data);

	String resetPassword(Map<String, String> userData);

	UserForClientResponseDto findByUsernameForClient(String username);
	
	String banned(Long uno);

	String findPassword(String string);

	String remove(Long uno, Long userId);

	StatisticsDto statsInSevenDays(Long userId);

	void createTracker();
	
	void removedYearOldDeleted();
}