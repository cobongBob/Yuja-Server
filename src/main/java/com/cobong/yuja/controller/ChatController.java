package com.cobong.yuja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {
	@GetMapping("/socket/chat")
	public String index() {
		return "chatting/chatroom";
	}
	
	@GetMapping("/socket/chat/ex")
	public String exFromWeb() {
		return "chatting/exs";
	}
}
