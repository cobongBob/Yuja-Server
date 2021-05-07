package com.cobong.yuja.config;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cobong.yuja.service.board.BoardAttachService;
import com.cobong.yuja.service.board.ThumbnailService;
import com.cobong.yuja.service.user.ProfilePictureService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AutomaticFileDeletion {
	private final BoardAttachService boardAttachService;
	private final ProfilePictureService profilePictureService;
	private final ThumbnailService thumbnailService;
	
	@Scheduled(cron = "0 43 17 * * *")
	public void deleteAtFourAM() {
		File tempToDel = Paths.get(System.getProperty("user.dir") + File.separator+"files" + File.separator +"temp").toFile();
		File[] tempsToDel = tempToDel.listFiles();
		for(File file: tempsToDel) {
			file.delete();			
		}
		tempToDel.delete();
		
		boardAttachService.deleteUnflagged();
		profilePictureService.deleteUnflagged();
		thumbnailService.deleteUnflagged();
	}
}