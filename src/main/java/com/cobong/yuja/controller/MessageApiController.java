//package com.cobong.yuja.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/api/message")
//@RequiredArgsConstructor
//public class MessageApiController {
//
//	
//	@PostMapping("/send")
//	public ResponseEntity<?> postMessage() {
//		return new ResponseEntity<>(HttpStatus.CREATED);
//	}
//	
//	@GetMapping("/")
//	public ResponseEntity<?> getOneMessage() {
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//	
//	@GetMapping("/")
//	public ResponseEntity<?> getAllMessage() {
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//	
//	@DeleteMapping("/")
//	public ResponseEntity<?> deleteMessage() {
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//}