package com.SmartHostel.controller;

import com.SmartHostel.model.*;
import com.SmartHostel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private ComplaintService complaintService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private MessMenuService messMenuService;

	@Autowired
	private HostelService hostelService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private AuditService auditService;

	@Autowired
	private AnalyticsService analyticsService;

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			model.addAttribute("user", userOpt.get());
			
			// Get statistics
			List<User> users = userService.findAll();
			List<Student> students = studentService.getAllStudents();
			List<Complaint> complaints = complaintService.getAllComplaints();
			List<Room> rooms = roomService.getAllRooms();
			
			model.addAttribute("totalUsers", users.size());
			model.addAttribute("totalStudents", students.size());
			model.addAttribute("totalComplaints", complaints.size());
			model.addAttribute("totalRooms", rooms.size());
			
			// Add analytics data
			Map<String, Object> analytics = analyticsService.getDashboardAnalytics("ADMIN", userOpt.get());
			model.addAttribute("analytics", analytics);
		}
		return "admin_dashboard_enhanced";
	}

	@GetMapping("/users")
	public String viewUsers(Model model) {
		List<User> users = userService.findAll();
		model.addAttribute("users", users);
		return "admin_users";
	}

	@GetMapping("/students")
	public String viewStudents(Model model) {
		List<Student> students = studentService.getAllStudents();
		model.addAttribute("students", students);
		return "admin_students";
	}

	@GetMapping("/rooms")
	public String viewRooms(Model model) {
		List<Room> rooms = roomService.getAllRooms();
		model.addAttribute("rooms", rooms);
		return "admin_rooms";
	}

	@GetMapping("/rooms/new")
	public String newRoomForm(Model model) {
		model.addAttribute("room", new Room());
		return "admin_room_form";
	}

	@PostMapping("/rooms")
	public String saveRoom(@RequestParam String roomNumber,
						  @RequestParam String hostelName,
						  @RequestParam String floor,
						  @RequestParam Integer capacity,
						  @RequestParam String roomType,
						  @RequestParam Double rent) {
		Room room = new Room();
		room.setRoomNumber(roomNumber);
		room.setHostelName(hostelName);
		room.setFloor(floor);
		room.setCapacity(capacity);
		room.setRoomType(roomType);
		room.setRent(rent);
		room.setOccupied(0);
		room.setAvailable(true);
		roomService.saveRoom(room);
		return "redirect:/admin/rooms";
	}

	@PostMapping("/rooms/{id}/delete")
	public String deleteRoom(@PathVariable Long id) {
		roomService.deleteRoom(id);
		return "redirect:/admin/rooms";
	}

	@GetMapping("/complaints")
	public String viewComplaints(Model model) {
		List<Complaint> complaints = complaintService.getAllComplaints();
		model.addAttribute("complaints", complaints);
		return "admin_complaints";
	}

	@GetMapping("/payments")
	public String viewPayments(Model model) {
		List<Payment> payments = paymentService.getAllPayments();
		model.addAttribute("payments", payments);
		return "admin_payments";
	}

	@GetMapping("/mess-menu")
	public String viewMessMenu(Model model) {
		List<MessMenu> menus = messMenuService.getAllMenus();
		model.addAttribute("menus", menus);
		return "admin_mess_menu";
	}

	@GetMapping("/mess-menu/new")
	public String newMessMenuForm(Model model) {
		model.addAttribute("menu", new MessMenu());
		return "admin_mess_menu_form";
	}

	@PostMapping("/mess-menu")
	public String saveMessMenu(@RequestParam String date,
							   @RequestParam String breakfast,
							   @RequestParam String lunch,
							   @RequestParam String dinner,
							   @RequestParam(required = false) String snacks) {
		MessMenu menu = new MessMenu();
		menu.setDate(java.time.LocalDate.parse(date));
		menu.setDayOfWeek(java.time.LocalDate.parse(date).getDayOfWeek().toString());
		menu.setBreakfast(breakfast);
		menu.setLunch(lunch);
		menu.setDinner(dinner);
		if (snacks != null && !snacks.isEmpty()) {
			menu.setSnacks(snacks);
		}
		messMenuService.saveMenu(menu);
		return "redirect:/admin/mess-menu";
	}

	@GetMapping("/hostels")
	public String viewHostels(Model model) {
		List<Hostel> hostels = hostelService.getAllHostels();
		model.addAttribute("hostels", hostels);
		return "admin_hostels";
	}

	@PostMapping("/hostels/{id}/approve")
	public String approveHostel(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
		Optional<Hostel> hostelOpt = hostelService.getHostelById(id);
		Optional<User> adminOpt = userService.findByEmail(authentication.getName());
		
		if (hostelOpt.isPresent() && adminOpt.isPresent()) {
			Hostel hostel = hostelOpt.get();
			hostel.setStatus("ACTIVE");
			hostel.setApprovedAt(java.time.LocalDateTime.now());
			hostelService.saveHostel(hostel);

			// Notify warden
			if (hostel.getWarden() != null) {
				notificationService.createNotification(
					hostel.getWarden(),
					"Hostel Approved",
					"Your hostel '" + hostel.getName() + "' has been approved by admin.",
					"SUCCESS",
					"SYSTEM",
					"/warden/hostels"
				);
			}

			// Audit log
			auditService.logAction(adminOpt.get(), "APPROVE", "HOSTEL", id, 
				"PENDING_APPROVAL", "ACTIVE", null);

			redirectAttributes.addFlashAttribute("success", "Hostel approved successfully!");
		}
		return "redirect:/admin/hostels";
	}

	@PostMapping("/hostels/{id}/reject")
	public String rejectHostel(@PathVariable Long id, @RequestParam String reason, 
							   Authentication authentication, RedirectAttributes redirectAttributes) {
		Optional<Hostel> hostelOpt = hostelService.getHostelById(id);
		Optional<User> adminOpt = userService.findByEmail(authentication.getName());
		
		if (hostelOpt.isPresent() && adminOpt.isPresent()) {
			Hostel hostel = hostelOpt.get();
			hostel.setStatus("REJECTED");
			hostelService.saveHostel(hostel);

			// Notify warden
			if (hostel.getWarden() != null) {
				notificationService.createNotification(
					hostel.getWarden(),
					"Hostel Registration Rejected",
					"Your hostel '" + hostel.getName() + "' registration was rejected. Reason: " + reason,
					"ERROR",
					"SYSTEM",
					"/warden/hostels"
				);
			}

			redirectAttributes.addFlashAttribute("success", "Hostel rejected.");
		}
		return "redirect:/admin/hostels";
	}

	@GetMapping("/bookings")
	public String viewAllBookings(Model model) {
		List<Booking> allBookings = bookingService.getAllBookings();
		model.addAttribute("bookings", allBookings != null ? allBookings : new java.util.ArrayList<>());
		return "admin_bookings";
	}

	@GetMapping("/audit-logs")
	public String viewAuditLogs(Model model) {
		// Will be implemented with pagination
		return "admin_audit_logs";
	}
}

