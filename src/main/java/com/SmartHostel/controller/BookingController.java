package com.SmartHostel.controller;

import com.SmartHostel.model.*;
import com.SmartHostel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class BookingController {

	@Autowired
	private UserService userService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private HostelService hostelService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private RecommendationService recommendationService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private RoomService roomService;

	@GetMapping("/bookings")
	public String viewBookings(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent()) {
				List<Booking> bookings = bookingService.getBookingsByStudent(studentOpt.get());
				model.addAttribute("bookings", bookings != null ? bookings : new java.util.ArrayList<>());
			}
		}
		return "student_bookings";
	}

	@GetMapping("/bookings/new")
	public String showBookingForm(Model model) {
		List<Hostel> hostels = hostelService.getAllHostels();
		model.addAttribute("hostels", hostels);
		return "student_booking_form";
	}

	@PostMapping("/bookings")
	public String createBooking(Authentication authentication,
							   @RequestParam Long hostelId,
							   @RequestParam Long roomId,
							   @RequestParam String checkInDate,
							   @RequestParam String checkOutDate,
							   @RequestParam(required = false) String preferences) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			Optional<Hostel> hostelOpt = hostelService.getHostelById(hostelId);
			Optional<Room> roomOpt = roomService.getRoomById(roomId);
			
			if (studentOpt.isPresent() && hostelOpt.isPresent() && roomOpt.isPresent()) {
				Booking booking = bookingService.createBooking(
					studentOpt.get(),
					hostelOpt.get(),
					roomOpt.get(),
					LocalDate.parse(checkInDate),
					LocalDate.parse(checkOutDate),
					preferences
				);
				
				// Send notification
				notificationService.createNotification(
					userOpt.get(),
					"Booking Created",
					"Your booking request has been submitted successfully.",
					"SUCCESS",
					"BOOKING",
					"/student/bookings"
				);
				
				return "redirect:/student/bookings";
			}
		}
		return "redirect:/student/bookings/new?error=true";
	}

	@GetMapping("/recommendations")
	public String getRecommendations(Authentication authentication,
									@RequestParam(required = false) String preferences,
									Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent()) {
				List<Hostel> recommendations = recommendationService.getRecommendedHostels(
					studentOpt.get(),
					preferences != null ? preferences : "{}"
				);
				model.addAttribute("recommendations", recommendations);
				model.addAttribute("preferences", preferences);
			}
		}
		return "student_recommendations";
	}
}
