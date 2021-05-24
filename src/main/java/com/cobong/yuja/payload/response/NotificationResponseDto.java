package com.cobong.yuja.payload.response;

import java.util.Date;

import com.cobong.yuja.model.Notification;
import com.cobong.yuja.payload.response.comment.CommentResponseDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
	private Long notiId;
	private UserResponseDto sender;
	private UserResponseDto resipeint;
	private CommentResponseDto comment;
	private String type;
	private Date readDate;
	
	public NotificationResponseDto entityToDto(Notification entity) {
		this.notiId = entity.getNotiId();
		this.resipeint = new UserResponseDto().entityToDto(entity.getRecipient());
		this.sender = new UserResponseDto().entityToDto(entity.getSender());
		this.type = entity.getType();
		this.readDate = entity.getReadDate();
		this.comment = (entity.getComment() != null)? new CommentResponseDto().entityToDto(entity.getComment()): null;
		return this;
	}
}
