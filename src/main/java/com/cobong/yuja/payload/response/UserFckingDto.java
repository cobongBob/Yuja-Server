package com.cobong.yuja.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFckingDto {
	private String userName;
	private Long userId;
	private Long authorityId;
}
