package com.cobong.yuja.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.config.auth.PrincipalDetails;
import com.cobong.yuja.config.jwt.CookieProvider;
import com.cobong.yuja.payload.request.board.BoardSaveRequestDto;
import com.cobong.yuja.payload.request.board.BoardUpdateRequestDto;
import com.cobong.yuja.service.board.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardApiController {
	private final BoardService boardService;
	private final CookieProvider cookieProvider;
	
	@PostMapping("/api/{boardCode}/board") // done
	public ResponseEntity<?> insertBoard(@RequestBody BoardSaveRequestDto dto, @PathVariable Long boardCode) {
		dto.setBoardCode(boardCode);
		return new ResponseEntity<>(boardService.save(dto),HttpStatus.CREATED);
	}
	
	@GetMapping("/api/{boardCode}/board/{bno}")
	public ResponseEntity<?> getOneBoard(@PathVariable Long boardCode, @PathVariable Long bno,
			HttpServletResponse res,HttpServletRequest req) {
		
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	boolean ishit = false;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
    	Cookie hitCookie = cookieProvider.getCookie(req, "hitCookieBno");
    	if(hitCookie != null && hitCookie.getValue().equals(String.valueOf(bno))) ishit = true;
    	hitCookie = cookieProvider.createHitCookie("hitCookieBno", bno);
    	res.addCookie(hitCookie);
		return new ResponseEntity<>(boardService.findById(bno,userId,ishit),HttpStatus.OK);
	}
	
	@GetMapping("/api/board/allBoard")
	public ResponseEntity<?> getAllBoard(){
		return new ResponseEntity<>(boardService.findAll(),HttpStatus.OK);
	}
	
	@PutMapping("/api/{boardCode}/board/{bno}") // done
	public ResponseEntity<?> modifyBoard(@PathVariable Long boardCode, @PathVariable Long bno, @RequestBody BoardUpdateRequestDto boardUpdateRequestDto){
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(boardService.modify(bno,boardUpdateRequestDto, userId),HttpStatus.OK);
	}
	
    @DeleteMapping("/api/{boardCode}/board/{bno}") // done
	public ResponseEntity<?> deleteBoard(@PathVariable Long boardCode, @PathVariable Long bno){
    	PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(boardService.delete(bno, userId),HttpStatus.OK);
	}
	
	@GetMapping("/api/{boardCode}/board") // done 
	public ResponseEntity<?> boardsInBoardType(@PathVariable Long boardCode){
		/***
		 * ????????? ?????????(?????????,?????????, ????????????)??? ?????? ??? ???????????? ????????????
		 * ????????? ??? ????????????.. ?????? ?????? ?????? ???????????? ?????? ??? 
		 * Security ????????? ?????????????????? ??????/???????????????????????? ???????????? ???????????? ?????????????????? ??????????????????
		 * ???????????? ????????? ???????????? ???????????? ???????????? ???????????? ?????? 
		 */
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(boardService.boardsInBoardType(boardCode,userId), HttpStatus.OK);
	}
	
	@GetMapping("/api/user/board/{boardCode}/{userId}") //done
	public ResponseEntity<?> boardsUserWrote(@PathVariable Long userId, @PathVariable Long boardCode){
		/***
		 * ?????? ?????? ????????? ?????? ????????????. ?????? ????????? ???????????? ???????????? ????????? ????????? ???????????? api??? ????????????.
		 */
		return new ResponseEntity<>(boardService.boardsUserWrote(userId, boardCode), HttpStatus.OK);
	}
	
	@GetMapping("/api/user/likedBy/{userId}") //done
	public ResponseEntity<?> boardsUserLiked(@PathVariable Long userId){
		/***
		 *  ????????? ???/????????? ??? ???????????? ?????? ???????????? ????????????
		 */
		return new ResponseEntity<>(boardService.boardsUserLiked(userId), HttpStatus.OK);
	}
	
	@GetMapping("/api/user/commentedBy/{userId}") //done
	public ResponseEntity<?> boardsUserCommented(@PathVariable Long userId){
		return new ResponseEntity<>(boardService.boardsUserCommented(userId), HttpStatus.OK);
	}
	
	@GetMapping("/api/main/board") // done
	public ResponseEntity<?> getMainBoardData(HttpServletResponse res, HttpServletRequest req) {
		PrincipalDetails principalDetails = null;
	       Long userId = 0L;
	       boolean isVisit = false;
	       if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
	          principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	         userId = principalDetails.getUserId();
	      }
	       Cookie visitCookie = cookieProvider.getCookie(req, "visitCookieBno");
	       if(visitCookie != null) {
	          isVisit = true;
	       }
	       visitCookie = cookieProvider.createVisitCookie("visitCookieBno", String.valueOf(userId));
	       res.addCookie(visitCookie);
		return new ResponseEntity<>(boardService.getMainBoardData(isVisit), HttpStatus.OK);
	}
	@GetMapping("/api/notice/private/{bno}") //done
	public ResponseEntity<?> setNoticePrivate(@PathVariable Long bno) {
		return new ResponseEntity<>(boardService.noticePrivateSwitch(bno), HttpStatus.OK);
	}
}
