package com.cobong.yuja.config.websocket;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
	private String username;
	private String content;
	private Date date;
}
