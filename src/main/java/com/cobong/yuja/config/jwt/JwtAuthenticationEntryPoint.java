package com.cobong.yuja.config.jwt;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.cobong.yuja.exception.dto.ExceptionResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 필요한 resource에 엑세스 하려고 시도 중 예외처리 class
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = 
			LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("예외처리 메시지 내용 -> {}", authException.getMessage());
		ExceptionResponseDto dto = new ExceptionResponseDto(401, "Unauthorized error: god damn mother fucking Exception... give me a damn mercy");
		OutputStream out = response.getOutputStream();
		ObjectMapper om = new ObjectMapper();
		om.writeValue(out, dto);
		out.flush();
		
	}
}
