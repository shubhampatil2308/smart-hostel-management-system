package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendRegistrationEmail(String toEmail, String username, String role) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject("Welcome to Smart Hostel Management System");
			message.setText("Dear " + username + ",\n\n" +
					"Welcome to Smart Hostel Management System!\n\n" +
					"Your account has been successfully registered as " + role + ".\n\n" +
					"You can now login to the system using your registered email and password.\n\n" +
					"Thank you for choosing Smart Hostel Management System.\n\n" +
					"Best Regards,\n" +
					"Smart Hostel Management Team");
			message.setFrom("noreply@smarthostel.com");
			
			mailSender.send(message);
		} catch (Exception e) {
			System.err.println("Error sending email: " + e.getMessage());
		}
	}

	public void sendComplaintResponseEmail(String toEmail, String username, String complaintSubject, String response) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject("Response to Your Complaint: " + complaintSubject);
			message.setText("Dear " + username + ",\n\n" +
					"Your complaint regarding '" + complaintSubject + "' has been reviewed.\n\n" +
					"Response: " + response + "\n\n" +
					"Thank you for your patience.\n\n" +
					"Best Regards,\n" +
					"Smart Hostel Management Team");
			message.setFrom("noreply@smarthostel.com");
			
			mailSender.send(message);
		} catch (Exception e) {
			System.err.println("Error sending email: " + e.getMessage());
		}
	}

	public void sendNotificationEmail(String toEmail, String title, String message) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(toEmail);
			mailMessage.setSubject("Smart Hostel Notification: " + title);
			mailMessage.setText(message);
			mailMessage.setFrom("noreply@smarthostel.com");
			
			mailSender.send(mailMessage);
		} catch (Exception e) {
			System.err.println("Error sending notification email: " + e.getMessage());
		}
	}
}

