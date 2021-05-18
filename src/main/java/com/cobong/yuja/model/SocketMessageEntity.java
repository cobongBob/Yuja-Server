//package com.cobong.yuja.model;
//
//import java.time.LocalDateTime;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class SocketMessageEntity {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long socketMsgIdp;
//	
//	@Column(length = 5000, nullable = false)
//	private String message;
//	private LocalDateTime time;
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name="roomId")
//	private ChatRoom chatRoom; 
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name="userId")
//	private User writer;
//}
