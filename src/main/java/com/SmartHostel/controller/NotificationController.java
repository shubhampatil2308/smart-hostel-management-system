package com.SmartHostel.controller;

import com.SmartHostel.model.*;
import com.SmartHostel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class NotificationController {

	@Autowired
	private UserService userService;

	@Autowired
	private NotificationService notificationService;

	@GetMapping("/notifications")
	public String viewNotifications(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			List<Notification> notifications = notificationService.getNotificationsByUser(userOpt.get());
			model.addAttribute("notifications", notifications != null ? notifications : new java.util.ArrayList<>());
			model.addAttribute("unreadCount", notificationService.getUnreadNotifications(userOpt.get()).size());
		}
		return "notifications";
	}

	@PostMapping("/notifications/{id}/read")
	public String markAsRead(@PathVariable Long id) {
		notificationService.markAsRead(id);
		return "redirect:/notifications";
	}

	@PostMapping("/notifications/read-all")
	public String markAllAsRead(Authentication authentication) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			notificationService.markAllAsRead(userOpt.get());
		}
		return "redirect:/notifications";
	}
}
