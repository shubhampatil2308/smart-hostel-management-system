package com.SmartHostel.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketNotificationController {

	@MessageMapping("/notifications")
	@SendTo("/topic/notifications")
	public String sendNotification(String message) {
		return message;
	}
}

