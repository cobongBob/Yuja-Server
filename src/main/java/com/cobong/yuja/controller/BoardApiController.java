package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.payload.request.board.BoardSaveRequestDto;
import com.cobong.yuja.payload.request.board.BoardUpdateRequestDto;
import com.cobong.yuja.service.board.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardApiController {
	private final BoardService boardService;
	
	@PostMapping("/api/{boardCode}/board/{bno}")
	public ResponseEntity<?> insertBoard(@RequestBody BoardSaveRequestDto dto, @PathVariable Long boardCode) {
		dto.setBoardCode(boardCode);
		return new ResponseEntity<>(boardService.save(dto),HttpStatus.CREATED);
	}
	
	@GetMapping("/api/{boardCode}/board/{bno}")
	public ResponseEntity<?> getOneBoard(@PathVariable Long boardCode, @PathVariable Long bno) {
		return new ResponseEntity<>(boardService.findById(bno),HttpStatus.OK);
	}
	
	@GetMapping("/api/board/allBoard")
	public ResponseEntity<?> getAllBoard(){
		return new ResponseEntity<>(boardService.findAll(),HttpStatus.OK);
	}
	
	@PutMapping("/api/{boardCode}/board/{bno}")
	public ResponseEntity<?> modifyBoard(@PathVariable Long boardCode, @PathVariable Long bno, @RequestBody BoardUpdateRequestDto boardUpdateRequestDto){
		return new ResponseEntity<>(boardService.modify(bno,boardUpdateRequestDto),HttpStatus.OK);
	}
	
    @DeleteMapping("/api/{boardCode}/board/{bno}")
	public ResponseEntity<?> deleteBoard(@PathVariable Long boardCode, @PathVariable Long bno){
		return new ResponseEntity<>(boardService.delete(bno),HttpStatus.OK);
	}
	
	@GetMapping("/api/{boardCode}/board")
	public ResponseEntity<?> boardsInBoardType(@PathVariable Long boardCode){
		/***
		 * 선택한 게시판(유튜버,편집자, 썸네일러)의 글을 다 떙겨오는 컨트롤러
		 * 이름이 좀 어색하당.. 좋은 이름 있음 바꿔주심 좋을 듯 
		 * Security 완성후 유저아이디를 쿠키/로컬스토리지에서 받아오는 방식으로 유저아이디를 받아와야한다
		 * 로그인한 유저가 좋아요를 눌렀는지 아닌지를 확인하기 위해
		 */
		return new ResponseEntity<>(boardService.boardsInBoardType(boardCode), HttpStatus.OK);
	}
	
	@GetMapping("/api/user/board/{userId}")
	public ResponseEntity<?> boardsUserWrote(@PathVariable Long userId){
		/***
		 * 매핑 받는 주소가 많이 걱정된다. 일단 하나의 보드만을 가져오는 친구와 차별을 두기위해 api를 추가했음.
		 */
		return new ResponseEntity<>(boardService.boardsUserWrote(userId), HttpStatus.OK);
	}
	
	@GetMapping("/api/user/likedBy/{userId}")
	public ResponseEntity<?> boardsUserLiked(@PathVariable Long userId){
		/***
		 *  유저가 찜/좋아요 한 게스글을 전부 불러오는 컨트롤러
		 */
		return new ResponseEntity<>(boardService.boardsUserLiked(userId), HttpStatus.OK);
	}
	
	@GetMapping("/api/user/commentedBy/{userId}")
	public ResponseEntity<?> boardsUserCommented(@PathVariable Long userId){
		return new ResponseEntity<>(boardService.boardsUserCommented(userId), HttpStatus.OK);
	}
}
