package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.*;
import com.SmartHostel.repository.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private ComplaintRepository complaintRepository;

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private HostelRepository hostelRepository;

	@Autowired
	private UserRepository userRepository;

	public Map<String, Object> getDashboardAnalytics(String role, User user) {
		Map<String, Object> analytics = new HashMap<>();
		
		if (role.equals("ADMIN")) {
			analytics.put("totalBookings", bookingRepository.count());
			analytics.put("totalRevenue", calculateTotalRevenue());
			analytics.put("occupancyRate", calculateOccupancyRate());
			analytics.put("pendingComplaints", complaintRepository.findByStatus("PENDING").size());
			analytics.put("activeHostels", hostelRepository.findByStatus("ACTIVE").size());
			analytics.put("bookingTrends", getBookingTrends(30));
			analytics.put("revenueTrends", getRevenueTrends(30));
			analytics.put("complaintDistribution", getComplaintStatusDistribution());
		} else if (role.equals("WARDEN")) {
			List<Hostel> hostels = hostelRepository.findByWarden(user);
			analytics.put("myHostels", hostels.size());
			analytics.put("totalBookings", bookingRepository.findAll().stream()
				.filter(b -> hostels.contains(b.getHostel()))
				.count());
		} else if (role.equals("STUDENT")) {
			// Student-specific analytics
		}
		
		return analytics;
	}

	public Map<String, Long> getBookingTrends(int days) {
		LocalDate startDate = LocalDate.now().minusDays(days);
		return bookingRepository.findAll().stream()
			.filter(b -> b.getBookingDate().isAfter(startDate) || b.getBookingDate().isEqual(startDate))
			.collect(Collectors.groupingBy(
				b -> b.getBookingDate().toString(),
				Collectors.counting()
			));
	}

	public Map<String, Double> getRevenueTrends(int days) {
		LocalDate startDate = LocalDate.now().minusDays(days);
		return paymentRepository.findAll().stream()
			.filter(p -> p.getPaymentDate().toLocalDate().isAfter(startDate) || 
						p.getPaymentDate().toLocalDate().isEqual(startDate))
			.filter(p -> p.getStatus().equals("COMPLETED"))
			.collect(Collectors.groupingBy(
				p -> p.getPaymentDate().toLocalDate().toString(),
				Collectors.summingDouble(Payment::getAmount)
			));
	}

	public Map<String, Long> getComplaintStatusDistribution() {
		return complaintRepository.findAll().stream()
			.collect(Collectors.groupingBy(
				Complaint::getStatus,
				Collectors.counting()
			));
	}

	private Double calculateTotalRevenue() {
		return paymentRepository.findAll().stream()
			.filter(p -> p.getStatus().equals("COMPLETED"))
			.mapToDouble(Payment::getAmount)
			.sum();
	}

	private Double calculateOccupancyRate() {
		List<Room> rooms = roomService.getAllRooms();
		if (rooms.isEmpty()) return 0.0;
		
		int totalCapacity = rooms.stream().mapToInt(Room::getCapacity).sum();
		int totalOccupied = rooms.stream().mapToInt(Room::getOccupied).sum();
		
		return totalCapacity > 0 ? (double) totalOccupied / totalCapacity * 100 : 0.0;
	}

	@Autowired
	private RoomService roomService;
}
