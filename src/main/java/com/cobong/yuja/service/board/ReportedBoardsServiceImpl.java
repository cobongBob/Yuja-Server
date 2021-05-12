package com.cobong.yuja.service.board;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cobong.yuja.model.ReportedBoards;
import com.cobong.yuja.payload.request.board.ReportedBoardsRequestDto;
import com.cobong.yuja.payload.response.board.BoardResponseDto;
import com.cobong.yuja.payload.response.board.ReportedBoardsResponseDto;
import com.cobong.yuja.repository.ReportedRepository;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportedBoardsServiceImpl implements ReportedBoardsService {
	private final ReportedRepository reportedRepository;
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public String reported(ReportedBoardsRequestDto dto) {
		ReportedBoards reported = ReportedBoards.builder()
				.user(userRepository.findById(dto.getUserId())
						.orElseThrow(() -> new IllegalArgumentException("해당 유저 없음")))
				.board(boardRepository.findById(dto.getBoardId())
						.orElseThrow(() -> new IllegalArgumentException("해당 글 없음")))
				.reportedReason(dto.getReportedReason()).build();
		reportedRepository.save(reported);
		return "####Success#####";
		// 다른 서비스랑 맞추기
	}

	@Override
	@Transactional
	public List<ReportedBoardsResponseDto> findAll() {
		List<ReportedBoardsResponseDto> reportedboards = new ArrayList<ReportedBoardsResponseDto>();
		List<ReportedBoards> boards = reportedRepository.findAll();
		for (ReportedBoards r : boards) {
			ReportedBoardsResponseDto boardsResponseDto = new ReportedBoardsResponseDto().entitytoDto(r);
			reportedboards.add(boardsResponseDto);
		}
		return reportedboards;
	}

	// delete

}
