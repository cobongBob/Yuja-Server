package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.payload.request.user.MessageRequestDto;
import com.cobong.yuja.service.user.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageApiController {
	private final MessageService messageservice;
	
	@PostMapping("/api/message")
	public ResponseEntity<?> postMessage(@RequestBody MessageRequestDto dto) {
		return new ResponseEntity<>(messageservice.send(dto),HttpStatus.CREATED);
	}
	
	@GetMapping("/api/message")
	public ResponseEntity<?> getAllMessage() {
		return new ResponseEntity<>(messageservice.findAll(),HttpStatus.OK);
	}
	
	@GetMapping("/api/message/{bno}")
	public ResponseEntity<?> getOneMessage(@PathVariable Long bno) {
		return new ResponseEntity<>(messageservice.findById(bno),HttpStatus.OK);
	}
	
	
	@DeleteMapping("/api/message/{bno}")
	public ResponseEntity<?> deleteMessage(@PathVariable Long bno) {
		return new ResponseEntity<>(messageservice.delete(bno),HttpStatus.OK);
	}
}