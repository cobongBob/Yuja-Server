package com.cobong.yuja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.TooManyRequests;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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
		 * 현재 생각나는 발생 가능한 오류들은 캐치되고 있으나, 이 외의 상황이 발생할시 필요할수 있다. 
		 * 다만 사용에는 주의가 필요한 점이 있는데, IllegalErrorAccess가 try-catch문 안에서 발생했을시, catch문에서 
		 * illegalAccessError를 throw 하더라도 런타임으로 발생하기에 여기로 온다. 근데 또 항상 그런건 아닌듯하다.. 
		 */
	//}
	
	@ExceptionHandler(value = AuthenticationException.class)
	public ResponseEntity<?> passwordError(Exception e) {
		return new ResponseEntity<>(new ExceptionRestResponse(1200, "이메일이나 비밀번호가 일치하지 않습니다"), HttpStatus.INTERNAL_SERVER_ERROR);
		/***
		 * 로그인시 이메일과 비번이 일치하지 않을 때 발생하는 에러.
		 */
	}
	
	@ExceptionHandler(value = BadRequest.class)
	public ResponseEntity<?> error400(Exception e) {
		return new ResponseEntity<>(new ExceptionRestResponse(400, "유효하지 않은 요청입니다."), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = TooManyRequests.class)
	public ResponseEntity<?> error429(Exception e) {
		return new ResponseEntity<>(new ExceptionRestResponse(429, "한번만 눌러도 충분히 작동합니다!"), HttpStatus.TOO_MANY_REQUESTS);
	}
	
	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> error400ArgType(){
		return new ResponseEntity<>(new ExceptionRestResponse(400, "잘못된 요청입니다."), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value=NumberFormatException.class)
	public ResponseEntity<?> numbFormatError(){
		return new ResponseEntity<>(new ExceptionRestResponse(400, "잘못된 요청입니다."), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<?> allothers(){
		return new ResponseEntity<>(new ExceptionRestResponse(99999, "내도 몰러유"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Value
    public static class ExceptionRestResponse {
        int code;
        String message;
    }
}