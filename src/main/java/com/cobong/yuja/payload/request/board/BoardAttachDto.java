package com.cobong.yuja.payload.request.board;

import com.cobong.yuja.model.BoardAttach;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardAttachDto {
	private Long boardId;
	private String uploadPath;
	private String tempPath;
	private String fileName;
	private String origFilename;
	private boolean flag;

	public BoardAttach toEntitiy() {
		return BoardAttach.builder()
				.uploadPath(this.uploadPath)
				.fileName(this.fileName)
				.tempPath(this.tempPath)
				.origFilename(this.origFilename)
				.flag(this.flag)
				.build();
	}
}
