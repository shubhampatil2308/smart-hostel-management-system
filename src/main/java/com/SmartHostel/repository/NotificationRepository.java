package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.Notification;
import com.SmartHostel.model.User;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUser(User user);
	List<Notification> findByUserAndIsRead(User user, boolean isRead);
	List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
