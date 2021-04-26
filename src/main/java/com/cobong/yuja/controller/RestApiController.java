package com.cobong.yuja.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestApiController {
	@GetMapping("/")
	public String home() {
		return "<h1>home</h1>";
	}
}
