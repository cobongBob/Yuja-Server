package com.cobong.yuja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.TooManyRequests;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Value;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = IllegalAccessError.class)
	public ResponseEntity<?> illegalAccessController(IllegalAccessError e){
		return new ResponseEntity<>(new ExceptionRestResponse(500, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<?> illegalStateException(IllegalArgumentException e) {
		return new ResponseEntity<>(new ExceptionRestResponse(500, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//@ExceptionHandler(value = RuntimeException.class)
	//public ResponseEntity<?> runtimeException(Exception e) {
		//return new ResponseEntity<>(new ExceptionRestResponse(1403, e.getLocalizedMessage()), HttpStatus.OK);
		/**
		 * 현재는 회원가입시 해당 유저 아이디의 유저가 존재할떄 발생
		 * 현재 AccessDeniedException도 이 핸들러로 처리중.. AccessDeniedException 도 런타임으로 분류가 되는것인가.. 
		 * 아님 그냥 서버 내에서 런타임 오류의 종휴가 발생했기에 그냥 이 쪽으로 보내는건지 모르겠다..
		 * 
		 */
	//}
	
	@ExceptionHandler(value = AuthenticationException.class)
	public ResponseEntity<?> passwordError(Exception e) {
		return new ResponseEntity<>(new ExceptionRestResponse(1200, "이메일이나 비밀호가 일치하지 않습니다"), HttpStatus.INTERNAL_SERVER_ERROR);
		/***
		 * 로그인시 이메일과 비번이 일치하지 않을 때 발생하는 에러.
		 */
	}
	
	@ExceptionHandler(value = BadRequestException.class)
	public String error400(Exception e) {
		return "Bad Request 400 " + e.getMessage();
	}
	
	@ExceptionHandler(value = TooManyRequests.class)
	public ResponseEntity<?> error429(Exception e) {
		return new ResponseEntity<>(new ExceptionRestResponse(429, "Too Many Requests " + e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
	}
	
	@Value
    public static class ExceptionRestResponse {
        int code;
        String message;
    }
}