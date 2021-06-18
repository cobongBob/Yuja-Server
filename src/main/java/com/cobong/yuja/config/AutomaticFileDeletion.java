package com.cobong.yuja.config;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cobong.yuja.service.attach.BoardAttachService;
import com.cobong.yuja.service.attach.ProfilePictureService;
import com.cobong.yuja.service.attach.ThumbnailService;
import com.cobong.yuja.service.board.BoardService;
import com.cobong.yuja.service.notification.NotificationService;
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
	private final NotificationService notificationService;
	private final BoardService boardService;
	
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
		notificationService.delete2weeksOld();
		userService.removeYearOldDeleted();
		
		//마감일 지난 공고들 자동삭제
		boardService.deleteExpired();
	}
	
	@Scheduled(cron = "0 0 0 * * *")
	public void createAtMidnight() {
		
		userService.createTracker();
		
	}
}
