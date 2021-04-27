package com.cobong.yuja.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.payload.request.BoardUpdateRequestDto;
import com.cobong.yuja.payload.response.UserResponseDto;
import com.cobong.yuja.service.BoardService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class BoardApiController {
	private final BoardService boardService;
	
	
	@GetMapping("/")
	public String home() {
		return "<h1>home</h1>";
	}
	
	@PostMapping(path = "api/{boardCode}/board", consumes = "application/json")
	public ResponseEntity<?> insertBoard(@RequestBody BoardSaveRequestDto dto) {
		System.out.println("==========>"+dto);
		return new ResponseEntity<Board>(boardService.save(dto),HttpStatus.CREATED);
	}
	
	@GetMapping("api/{boardCode}/board/{bno}")
	public ResponseEntity<?> getOneBoard(@PathVariable Long bno) {
		System.out.println("==========>" + bno);
		return new ResponseEntity<Board>(boardService.get(bno),HttpStatus.OK);
	}
	
	@GetMapping("/api/user/getAll")
	public ResponseEntity<?> getAllBoard(){
		return new ResponseEntity<Board>(boardService.findAll(),HttpStatus.OK);
	}
	
	// delete
	@DeleteMapping("api/{boardCode}/board/{bno}")
	public ResponseEntity<?> deleteBoard(@PathVariable Long bno){
		return new ResponseEntity<String>(boardService.delete(bno),HttpStatus.OK);
	}

    // update
	@PutMapping("api/{boardCode}/board/{bno}")
	public ResponseEntity<?> modifyBoard(@PathVariable Long bno, @RequestBody BoardUpdateRequestDto boardUpdateRequestDto){
		return new ResponseEntity<Board>(boardService.modify(bno,boardUpdateRequestDto),HttpStatus.OK);
	}
}
