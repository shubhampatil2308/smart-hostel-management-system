package com.SmartHostel.controller;

import com.SmartHostel.model.*;
import com.SmartHostel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import com.SmartHostel.repository.BookingRepository;

@Controller
@RequestMapping("/warden")
public class CheckInOutController {

	@Autowired
	private UserService userService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private QRCodeService qrCodeService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private RoomService roomService;

	@GetMapping("/checkin")
	public String showCheckInForm(Model model) {
		return "warden_checkin";
	}

	@PostMapping("/checkin/{bookingId}")
	public String processCheckIn(@PathVariable Long bookingId, Authentication authentication) {
		Optional<Booking> bookingOpt = bookingService.getBookingById(bookingId);
		if (bookingOpt.isPresent()) {
			Booking booking = bookingOpt.get();
			booking.setCheckInTime(LocalDateTime.now());
			booking.setStatus("CONFIRMED");
			bookingRepository.save(booking);
			bookingService.confirmBooking(bookingId);
			
			// Send notification
			notificationService.createNotification(
				booking.getStudent().getUser(),
				"Check-In Successful",
				"You have successfully checked in to " + booking.getHostel().getName(),
				"SUCCESS",
				"BOOKING",
				"/student/bookings"
			);
			
			return "redirect:/warden/checkin?success=true";
		}
		return "redirect:/warden/checkin?error=true";
	}

	@PostMapping("/checkout/{bookingId}")
	public String processCheckOut(@PathVariable Long bookingId, Authentication authentication) {
		Optional<Booking> bookingOpt = bookingService.getBookingById(bookingId);
		if (bookingOpt.isPresent()) {
			Booking booking = bookingOpt.get();
			booking.setCheckOutTime(LocalDateTime.now());
			booking.setStatus("COMPLETED");
			
			// Update room occupancy
			if (booking.getRoom() != null) {
				Room room = booking.getRoom();
				room.setOccupied(Math.max(0, room.getOccupied() - 1));
				room.setAvailable(true);
				roomService.saveRoom(room);
			}
			bookingRepository.save(booking);
			
			// Send notification
			notificationService.createNotification(
				booking.getStudent().getUser(),
				"Check-Out Successful",
				"You have successfully checked out from " + booking.getHostel().getName(),
				"SUCCESS",
				"BOOKING",
				"/student/bookings"
			);
			
			return "redirect:/warden/checkout?success=true";
		}
		return "redirect:/warden/checkout?error=true";
	}

	@PostMapping("/checkin/qr")
	public String processQRCheckIn(@RequestParam String qrData, Authentication authentication) {
		// Parse QR data: BOOKING:ID:EMAIL
		String[] parts = qrData.split(":");
		if (parts.length >= 2) {
			try {
				Long bookingId = Long.parseLong(parts[1]);
				return processCheckIn(bookingId, authentication);
			} catch (NumberFormatException e) {
				return "redirect:/warden/checkin?error=invalid_qr";
			}
		}
		return "redirect:/warden/checkin?error=invalid_qr";
	}
}

