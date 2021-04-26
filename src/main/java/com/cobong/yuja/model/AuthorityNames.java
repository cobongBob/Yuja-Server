package com.cobong.yuja.model;

import lombok.Getter;

@Getter
public enum AuthorityNames {
	YOUTUBER(ROLES.YOUTUBER, "YOUTUBER AUTH"),
	EDITOR(ROLES.EDITOR, "EDITOR AUTH"), 
	THUMBNAIOR(ROLES.THUMBNAILOR, "THUMBNAILOR AUTH"), 
	ADMIN(ROLES.ADMIN, "ADMIN AUTH"), 
	GENERAL(ROLES.GENERAL, "GENERAL AUTH"), 
	MANAGER(ROLES.MANAGER, "MANAGER AUTH");

	public static class ROLES {
		public static final String YOUTUBER = "ROLE_YOUTUBER";
		public static final String EDITOR = "ROLE_EDITOR";
		public static final String THUMBNAILOR ="ROLE_THUMBNAILOR";
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
