package com.cobong.yuja.exception;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.cobong.yuja.exception.dto.ExceptionResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Forbidden403Exception implements AccessDeniedHandler {
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		ExceptionResponseDto dto = new ExceptionResponseDto(403, "권한이 없습니다");
		OutputStream out = response.getOutputStream();
		ObjectMapper om = new ObjectMapper();
		om.writeValue(out, dto);
		out.flush();
	}
}