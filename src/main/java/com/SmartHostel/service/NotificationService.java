package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.Notification;
import com.SmartHostel.model.User;
import com.SmartHostel.repository.NotificationRepository;
import java.util.List;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	public Notification createNotification(User user, String title, String message, 
										  String type, String category, String actionUrl) {
		Notification notification = new Notification();
		notification.setUser(user);
		notification.setTitle(title);
		notification.setMessage(message);
		notification.setType(type);
		notification.setCategory(category);
		notification.setActionUrl(actionUrl);
		return notificationRepository.save(notification);
	}

	public List<Notification> getNotificationsByUser(User user) {
		return notificationRepository.findByUserOrderByCreatedAtDesc(user);
	}

	public List<Notification> getUnreadNotifications(User user) {
		return notificationRepository.findByUserAndIsRead(user, false);
	}

	public void markAsRead(Long notificationId) {
		notificationRepository.findById(notificationId).ifPresent(notification -> {
			notification.setRead(true);
			notificationRepository.save(notification);
		});
	}

	public void markAllAsRead(User user) {
		List<Notification> notifications = notificationRepository.findByUserAndIsRead(user, false);
		notifications.forEach(n -> n.setRead(true));
		notificationRepository.saveAll(notifications);
	}
}
