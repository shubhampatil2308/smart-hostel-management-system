package com.SmartHostel.controller;

import com.SmartHostel.model.*;
import com.SmartHostel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/warden")
public class WardenController {

	@Autowired
	private UserService userService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private ComplaintService complaintService;

	@Autowired
	private AttendanceService attendanceService;

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			model.addAttribute("user", userOpt.get());
			
			// Get statistics
			List<Student> students = studentService.getAllStudents();
			List<Complaint> pendingComplaints = complaintService.getComplaintsByStatus("PENDING");
			List<Room> rooms = roomService.getAllRooms();
			
			model.addAttribute("totalStudents", students.size());
			model.addAttribute("pendingComplaints", pendingComplaints.size());
			model.addAttribute("totalRooms", rooms.size());
		}
		return "warden_dashboard";
	}

	@GetMapping("/students")
	public String viewStudents(Model model) {
		List<Student> students = studentService.getAllStudents();
		model.addAttribute("students", students != null ? students : new java.util.ArrayList<>());
		return "warden_students";
	}

	@GetMapping("/rooms")
	public String viewRooms(Model model) {
		List<Room> rooms = roomService.getAllRooms();
		model.addAttribute("rooms", rooms != null ? rooms : new java.util.ArrayList<>());
		return "warden_rooms";
	}

	@GetMapping("/rooms/new")
	public String newRoomForm(Model model) {
		model.addAttribute("room", new Room());
		return "warden_room_form";
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
		return "redirect:/warden/rooms";
	}

	@GetMapping("/complaints")
	public String viewComplaints(Model model) {
		List<Complaint> complaints = complaintService.getAllComplaints();
		model.addAttribute("complaints", complaints != null ? complaints : new java.util.ArrayList<>());
		return "warden_complaints";
	}

	@PostMapping("/complaints/{id}/resolve")
	public String resolveComplaint(@PathVariable Long id,
								   @RequestParam String response) {
		complaintService.updateComplaintStatus(id, "RESOLVED", response);
		return "redirect:/warden/complaints";
	}

	@GetMapping("/attendance")
	public String viewAttendance(Model model) {
		List<Attendance> attendance = attendanceService.getAllAttendance();
		model.addAttribute("attendance", attendance != null ? attendance : new java.util.ArrayList<>());
		return "warden_attendance";
	}

	@PostMapping("/attendance/mark")
	public String markAttendance(@RequestParam Long studentId,
								@RequestParam String status) {
		Optional<Student> studentOpt = studentService.getStudentById(studentId);
		if (studentOpt.isPresent()) {
			attendanceService.markAttendance(
				studentOpt.get(),
				LocalDate.now(),
				LocalTime.now(),
				status
			);
		}
		return "redirect:/warden/attendance";
	}
}

