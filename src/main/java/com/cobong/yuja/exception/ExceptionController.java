package com.cobong.yuja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.client.HttpClientErrorException.TooManyRequests;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Value;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<?> method1(IllegalArgumentException e) {
		return new ResponseEntity<>(new ExceptionRestResponse(1200, e.getMessage()), HttpStatus.OK);
	}
	
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<?> asdas(Exception e) {
		return new ResponseEntity<>(new ExceptionRestResponse(1500, e.getMessage()), HttpStatus.OK);
	}
	
	@ExceptionHandler(value = AuthenticationException.class)
	public ResponseEntity<?> asdsas(Exception e) {
		return new ResponseEntity<>(new ExceptionRestResponse(1200, e.getMessage()), HttpStatus.OK);
	}
	
	@ExceptionHandler(value = BadRequestException.class)
	public String error400(Exception e) {
		return "Bad Request 400 " + e.getMessage();
	}
	
	@ExceptionHandler(value = Unauthorized.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String error401(Exception e) {
		System.out.println("==============> "+e.getMessage());
		return "Access forbidden 401 " + e.getMessage();
	}
//	
	@ExceptionHandler(value = Forbidden.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<?>  error403(Exception e) {
		System.out.println("==============> "+e.getMessage());
		return new ResponseEntity<ExceptionRestResponse>(new ExceptionRestResponse(403, "Forbidden"),HttpStatus.FORBIDDEN);
	}
	
//	@ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
//	public String error415(Exception e) {
//		return "Unsupported Media Type Exception "+ e.getMessage();
//	}
	
	@ExceptionHandler(value = TooManyRequests.class)
	public String error429(Exception e) {
		return "Too Many Requests " + e.getMessage();
	}
	
	@ExceptionHandler(value = Exception.class)
	public String allExceptions(Exception e) {
		return "열로 잡아찌롱 "+e.getMessage();
	}
	
	@Value
    public static class ExceptionRestResponse {
        int code;
        String message;
    }
}