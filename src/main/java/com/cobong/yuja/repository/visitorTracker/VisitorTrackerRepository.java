package com.cobong.yuja.repository.visitorTracker;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cobong.yuja.model.VisitorTracker;

public interface VisitorTrackerRepository extends JpaRepository<VisitorTracker, Long>{
	@Query(nativeQuery = true, value ="SELECT * FROM VisitorTracker ORDER BY createdDate DESC LIMIT 1")
	Optional<VisitorTracker> findLastTracker();
	

	@Query(nativeQuery = true, value ="SELECT * FROM VisitorTracker ORDER BY createdDate DESC LIMIT 7")
	List<VisitorTracker> visitorsAfter();
}
