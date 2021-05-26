package com.cobong.yuja.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cobong.yuja.config.auth.PrincipalDetails;
import com.cobong.yuja.payload.request.chat.ChatRoomDto;
import com.cobong.yuja.payload.response.chat.SocketMessageSendDto;
import com.cobong.yuja.service.attach.ProfilePictureService;
import com.cobong.yuja.service.chat.ChatRoomJoinService;
import com.cobong.yuja.service.chat.ChatRoomService;
import com.cobong.yuja.service.chat.SocketMessageService;

import javassist.compiler.CompileError;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {
	private final ChatRoomService chatRoomService;
	private final SocketMessageService socketMessageService;
	private final ChatRoomJoinService chatRoomJoinService;
	private final ProfilePictureService profileService;
	
	@GetMapping("/rooms") // done
	public String rooms(HttpServletRequest req, Model model) throws CompileError {
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
    	if(userId == 0L) {
    		throw new CompileError("채팅을 시도하려는 유저가 존재하지 않거나 유저의 로그인 세션이 끝났습니다.");
    	}
		model.addAttribute("nickname",principalDetails.getNickname());
		model.addAttribute("userId", userId);
		/***
		 * getting current user done
		 */
		List<ChatRoomDto> chatRooms = chatRoomService.findRooms(userId);
		
		model.addAttribute("chatRooms", chatRooms);
		
		return "chatting/roomlist";
	}
	
	@PostMapping("/socket/room/client") // done
	public String newRoomFromReact(@RequestBody Map<String, String> receiver) {
		PrincipalDetails principalDetails = null;
    	Long user2Id = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			user2Id = principalDetails.getUserId();
		}
		Long chatRoomId = chatRoomService.newRoom(receiver.get("receiver"), user2Id);
		if(chatRoomId < 0) {
			return "redirect:/rooms";
		}
		return "redirect:/socket/chat/"+chatRoomId;
	}
	
	@PostMapping("/socket/room") // done
	public ResponseEntity<String> newRoom(@RequestParam("receiver") String receiver) {
		PrincipalDetails principalDetails = null;
    	Long user2Id = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			user2Id = principalDetails.getUserId();
		}
    	Long chatRoomId = chatRoomService.newRoom(receiver, user2Id);
		
		return new ResponseEntity<String>("/socket/chat/"+chatRoomId, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/socket/room/{roomId}") // done
	public ResponseEntity<List<ChatRoomDto>> deleteRoom(@PathVariable Long roomId) {
		chatRoomService.delete(roomId);
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<List<ChatRoomDto>>(chatRoomService.findRooms(userId), HttpStatus.OK);
	}
	
	@RequestMapping("/socket/chat/{chatRoomId}") // ?
	public String enterRoom(@PathVariable("chatRoomId") Long chatRoomId, Model model, HttpServletRequest req) {
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
    	if(userId == 0L) {
    		throw new IllegalAccessError("채팅을 시도하려는 유저가 존재하지 않거나 유저의 로그인 세션이 끝났습니다.");
    	}
		String userNickname = principalDetails.getNickname();
    	List<SocketMessageSendDto> messages = socketMessageService.getAllMsgs(chatRoomId, userId);
		String receiver = chatRoomJoinService.findReceiver(chatRoomId, userId);
		String senderPic = profileService.getProfileByName(userNickname);
		String receiverPic = profileService.getProfileByName(receiver);
		
		model.addAttribute("receiver", receiver);
		model.addAttribute("username", userNickname);
		model.addAttribute("messages", messages);
		model.addAttribute("roomId", chatRoomId);
		model.addAttribute("senderPic", senderPic);
		model.addAttribute("receiverPic", receiverPic);
		return "chatting/chatroom";
	}
	
	@ExceptionHandler(value = CompileError.class)
	public String chatSessionEndedError(CompileError e, Model model) {
		model.addAttribute("errorMsg", e.getMessage());
		return "chatting/errorPage";
	}
}





