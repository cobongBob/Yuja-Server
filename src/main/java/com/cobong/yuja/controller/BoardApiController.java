package com.cobong.yuja.controller;

import java.util.List;

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
import com.cobong.yuja.payload.response.BoardResponseDto;
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
	
//	@PostMapping("/api/{boardCode}/board/{bno}")
	@PostMapping(path = "/api/board")
	public ResponseEntity<?> insertBoard(@RequestBody BoardSaveRequestDto dto) {
		System.out.println("==========>"+dto);
		return new ResponseEntity<Board>(boardService.save(dto),HttpStatus.CREATED);
	}
	
//	@GetMapping("/api/{boardCode}/board/{bno}")
	@GetMapping("/{bno}")
	public ResponseEntity<?> getOneBoard(@PathVariable Long bno) {
		return new ResponseEntity<BoardResponseDto>(boardService.findById(bno),HttpStatus.OK);
	}
	
//	@GetMapping("/api/{boardCode}/board")
	@GetMapping("/api/board")
	public ResponseEntity<?> getAllBoard(){
		return new ResponseEntity<List<BoardResponseDto>>(boardService.findAll(),HttpStatus.OK);
	}
	
//	@PutMapping("/api/{boardCode}/board/{bno}")
	@PutMapping("/{bno}")
	public ResponseEntity<?> modifyBoard(@PathVariable Long bno, @RequestBody BoardUpdateRequestDto boardUpdateRequestDto){
		return new ResponseEntity<BoardResponseDto>(boardService.modify(bno,boardUpdateRequestDto),HttpStatus.OK);
	}
	
//  @DeleteMapping("/api/{boardCode}/board/{bno}")
	@DeleteMapping("/{bno}")
	public ResponseEntity<?> deleteBoard(@PathVariable Long bno){
		return new ResponseEntity<String>(boardService.delete(bno),HttpStatus.OK);
	}
	
	@GetMapping("/api/{boardCode}")
	public ResponseEntity<?> boardsInBoardType(@PathVariable Long boardCode){
		/***
		 * 선택한 게시판(유튜버,편집자, 썸네일러)의 글을 다 떙겨오는 컨트롤러
		 * 이름이 좀 어색하당.. 좋은 이름 있음 바꿔주심 좋을 듯 
		 */
		return new ResponseEntity<List<BoardResponseDto>>(boardService.boardsInBoardType(boardCode), HttpStatus.OK);
	}
	
	@GetMapping("/api/{userId}")
	public ResponseEntity<?> boardsUserWrote(@PathVariable Long userId){
		/***
		 * 매핑 받는 주소가 많이 걱정된다. 일단 하나의 보드만을 가져오는 친구와 차별을 두기위해 api를 추가했음.
		 */
		return new ResponseEntity<List<BoardResponseDto>>(boardService.boardsUserWrote(userId), HttpStatus.OK);
	}
	
	@GetMapping("/api/likedBy/{userId}")
	public ResponseEntity<?> boardsUserLiked(@PathVariable Long userId){
		/***
		 *  유저가 찜/좋아요 한 게스글을 전부 불러오는 컨트롤러
		 */
		return new ResponseEntity<List<BoardResponseDto>>(boardService.boardsUserLiked(userId), HttpStatus.OK);
	}
	
	@GetMapping("/api/commentedBy/{userId}")
	public ResponseEntity<?> boardsUserCommented(@PathVariable Long userId){
		return new ResponseEntity<List<BoardResponseDto>>(boardService.boardsUserCommented(userId), HttpStatus.OK);
	}
}
