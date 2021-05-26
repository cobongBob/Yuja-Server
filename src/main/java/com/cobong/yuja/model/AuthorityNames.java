package com.cobong.yuja.model;

import lombok.Getter;

@Getter
public enum AuthorityNames {
	YOUTUBER(ROLES.YOUTUBER, "ROLE_YOUTUBER"),
	EDITOR(ROLES.EDITOR, "ROLE_EDITOR"), 
	THUMBNAILER(ROLES.THUMBNAILER, "ROLE_THUMBNAILER"), 
	ADMIN(ROLES.ADMIN, "ROLE_ADMIN"), 
	GENERAL(ROLES.GENERAL, "ROLE_GENERAL"), 
	MANAGER(ROLES.MANAGER, "ROLE_MANAGER");

	public static class ROLES {
		public static final String YOUTUBER = "ROLE_YOUTUBER";
		public static final String EDITOR = "ROLE_EDITOR";
		public static final String THUMBNAILER ="ROLE_THUMBNAILER";
		public static final String GENERAL = "ROLE_GENERAL";
		public static final String ADMIN = "ROLE_ADMIN";
		public static final String MANAGER = "ROLE_MANAGER";
	}
	
	private String key;
	private String value;
	
	private AuthorityNames (String key, String value) {
		this.key = key;
		this.value= value;
	}
}
