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

	@Query("SELECT n FROM Notification n WHERE recipientId = :recipientId ORDER By notiId DESC")
	List<Notification> findByRecipientId(@Param("recipientId") Long recipientId);

	@Query("SELECT n.notiId FROM Notification n WHERE recipientId = :recipientId AND senderId =:senderId AND type =:type")
	Optional<Long> findByLastNoti(@Param("senderId") Long senderId,@Param("recipientId") Long recipientId,@Param("type") String type);
	
	@Modifying
	@Query(nativeQuery = true,value="DELETE FROM Notification WHERE recipientId=:receiverId AND senderId=:senderId AND type='chatNoti'")
	void deleteByRecipientId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
	
	@Query("SELECT n FROM Notification n WHERE n.type='chatNoti' AND n.recipient.userId=:recipientId")
	List<Notification> findChatNotiByRecipientId(@Param("recipientId") Long recipientId);
}
