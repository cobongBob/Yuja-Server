package com.cobong.yuja.repository.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cobong.yuja.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	// recipientId Unread Notification
	@Query(nativeQuery = true, value ="SELECT * FROM notification WHERE recipientId =:recipientId AND readDate IS NULL ORDER BY createdDate LIMIT 10")
	List<Notification> unreadNoti(@Param("recipientId") Long recipientId);

	@Modifying
	@Query(nativeQuery = true, value ="delete from Notification where commentId=:commentId")
	void deleteByCommentId(@Param("commentId") Long commentId);

	@Query("SELECT n FROM Notification n WHERE recipientId = :recipientId")
	List<Notification> findByRecipientId(@Param("recipientId") Long recipientId);

	@Query("SELECT n FROM Notification n WHERE recipientId = :recipientId AND senderId =:senderId AND type =:type")
	Optional<Notification> findByLastNoti(@Param("senderId") Long senderId,@Param("recipientId") Long recipientId,@Param("type") String type);
}
