package com.cobong.yuja.payload.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsDto {
	Long[] signedUp;
	Long[] visitors;
	Long[] totalBoards;
	Long[] userInc;
}
