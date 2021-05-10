package com.cobong.yuja.exception;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.cobong.yuja.exception.ExceptionController.ExceptionRestResponse;
import com.cobong.yuja.exception.dto.ExceptionResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Forbidden403Exception implements AccessDeniedHandler {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(Forbidden403Exception.class);
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		logger.error("\n=================   에러발생!!   =================\n");
		
		ExceptionResponseDto dto = new ExceptionResponseDto(403, "Fobidden error: god damn mother fucking Exception... give me a damn mercy");
		OutputStream out = response.getOutputStream();
		ObjectMapper om = new ObjectMapper();
		om.writeValue(out, dto);
		out.flush();
	}
}