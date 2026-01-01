package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.Booking;
import com.SmartHostel.model.Student;
import com.SmartHostel.model.Hostel;
import com.SmartHostel.model.Room;
import com.SmartHostel.repository.BookingRepository;
import com.SmartHostel.repository.RoomRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private QRCodeService qrCodeService;

	public List<Booking> getBookingsByStudent(Student student) {
		return bookingRepository.findByStudent(student);
	}

	public List<Booking> getBookingsByHostel(Hostel hostel) {
		return bookingRepository.findByHostel(hostel);
	}

	public Booking createBooking(Student student, Hostel hostel, Room room, 
								 LocalDate checkInDate, LocalDate checkOutDate, 
								 String preferences) {
		Booking booking = new Booking();
		booking.setStudent(student);
		booking.setHostel(hostel);
		booking.setRoom(room);
		booking.setCheckInDate(checkInDate);
		booking.setCheckOutDate(checkOutDate);
		booking.setPreferences(preferences);
		booking.setStatus("PENDING");
		
		// Calculate total amount
		if (room != null && room.getRent() != null) {
			long days = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
			booking.setTotalAmount(room.getRent() * days);
		}
		
		Booking savedBooking = bookingRepository.save(booking);
		
		// Generate QR code after saving (so we have the ID)
		String qrData = "BOOKING:" + savedBooking.getId() + ":" + student.getUser().getEmail();
		savedBooking.setQrCode(qrCodeService.generateQRCode(qrData));
		
		return bookingRepository.save(savedBooking);
	}

	public Booking confirmBooking(Long bookingId) {
		Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
		if (bookingOpt.isPresent()) {
			Booking booking = bookingOpt.get();
			booking.setStatus("CONFIRMED");
			
			// Update room occupancy
			if (booking.getRoom() != null) {
				Room room = booking.getRoom();
				room.setOccupied(room.getOccupied() + 1);
				if (room.getOccupied() >= room.getCapacity()) {
					room.setAvailable(false);
				}
				roomRepository.save(room);
			}
			
			return bookingRepository.save(booking);
		}
		return null;
	}

	public Optional<Booking> getBookingById(Long id) {
		return bookingRepository.findById(id);
	}

	public List<Booking> getAllBookings() {
		return bookingRepository.findAll();
	}
}
