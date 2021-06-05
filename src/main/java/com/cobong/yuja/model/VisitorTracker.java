package com.cobong.yuja.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.cobong.yuja.model.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class VisitorTracker extends DateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long visitorTrackId;
	
	Long visitorsToday;
	
	public void addNum() {
		this.visitorsToday++;
	}
}
