package com.cobong.yuja.payload.request.board;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardAttach;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardAttachDto {
	private Long boardId;
	private String uploadPath;
	private String fileName;
	private String origFilename;

	public BoardAttach toEntitiy() {
		return BoardAttach.builder()
				.uploadPath(this.uploadPath)
				.fileName(this.fileName)
				.origFilename(this.origFilename)
				.build();
	}
	
}
