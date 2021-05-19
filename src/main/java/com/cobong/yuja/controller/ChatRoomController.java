package com.cobong.yuja.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cobong.yuja.config.auth.PrincipalDetails;
import com.cobong.yuja.payload.request.chat.ChatRoomDto;
import com.cobong.yuja.payload.response.chat.SocketMessageSendDto;
import com.cobong.yuja.service.chat.ChatRoomJoinService;
import com.cobong.yuja.service.chat.ChatRoomService;
import com.cobong.yuja.service.chat.SocketMessageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {
	private final ChatRoomService chatRoomService;
	private final SocketMessageService socketMessageService;
	private final ChatRoomJoinService chatRoomJoinService;
	
	@GetMapping("/rooms")
	public String rooms(HttpServletRequest req, Model model) {
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
    	if(userId == 0L) {
    		throw new IllegalAccessError("채팅을 시도하려는 유저가 존재하지 않거나 유저의 로그인 세션이 끝났습니다.");
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
	
	@PostMapping("/socket/room")      //senderId의 경우 HttpServletRequest req로 받아서 현재 로그인 해있는 유저로 받아야한다.
	public String newRoom(@RequestParam("receiver") Long user1id, @RequestParam("sender") Long user2id) {
		Long chatRoomId = chatRoomService.newRoom(user1id, user2id);
		return "redirect:/socket/chat/"+chatRoomId;
	}
	
	@RequestMapping("/socket/chat/{chatRoomId}")
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
		
		model.addAttribute("receiver", receiver);
		model.addAttribute("username", userNickname);
		model.addAttribute("messages", messages);
		model.addAttribute("roomId", chatRoomId);
		
		return "chatting/chatroom";
	}
}





