package com.cobong.yuja.config;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cobong.yuja.service.NotificationService;
import com.cobong.yuja.service.board.BoardAttachService;
import com.cobong.yuja.service.board.ThumbnailService;
import com.cobong.yuja.service.chat.ChatRoomService;
import com.cobong.yuja.service.chat.SocketMessageService;
import com.cobong.yuja.service.user.ProfilePictureService;
import com.cobong.yuja.service.user.UserService;
import com.cobong.yuja.service.user.YoutubeConfirmService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AutomaticFileDeletion {
	private final BoardAttachService boardAttachService;
	private final ProfilePictureService profilePictureService;
	private final ThumbnailService thumbnailService;
	private final UserService userService;
	private final YoutubeConfirmService youtuberConfirmService;
	private final SocketMessageService socketMessageService;
	private final ChatRoomService chatRoomService;
	private final NotificationService notificationService;
	
	@Scheduled(cron = "0 0 4 * * *")
	public void deleteAtFourAM() {
		File tempToDel = Paths.get(System.getProperty("user.dir") + File.separator+"files" + File.separator +"temp").toFile();
		File[] tempsToDel = tempToDel.listFiles();
		if(tempsToDel != null && tempsToDel.length != 0) {
			for(File file: tempsToDel) {
				file.delete();			
			}
			tempToDel.delete();
		}
		
		boardAttachService.deleteUnflagged();
		profilePictureService.deleteUnflagged();
		thumbnailService.deleteUnflagged();
		userService.deleteUnflagged();
		youtuberConfirmService.deleteUnflagged();
		socketMessageService.delete2weeksOld();
		chatRoomService.deleteEmptyRooms();
		notificationService.delete2weeksOld();
		
		//마감일 기준 지난 공고들 자동삭제
	}
}
