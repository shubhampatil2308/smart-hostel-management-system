package com.SmartHostel.controller;

import com.SmartHostel.model.*;
import com.SmartHostel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private UserService userService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private ComplaintService complaintService;

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private MessMenuService messMenuService;

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			model.addAttribute("user", userOpt.get());
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent()) {
				Student student = studentOpt.get();
				model.addAttribute("student", student);
				
				// Get recent complaints
				List<Complaint> complaints = complaintService.getComplaintsByStudent(student);
				model.addAttribute("complaints", complaints != null ? complaints : new ArrayList<>());
				
				// Get recent payments
				List<Payment> payments = paymentService.getPaymentsByStudent(student);
				model.addAttribute("payments", payments != null ? payments : new ArrayList<>());
			} else {
				model.addAttribute("complaints", new ArrayList<>());
				model.addAttribute("payments", new ArrayList<>());
			}
		}
		return "student_dashboard";
	}

	@GetMapping("/room")
	public String viewRoom(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent()) {
				model.addAttribute("student", studentOpt.get());
			} else {
				model.addAttribute("student", null);
			}
		} else {
			model.addAttribute("student", null);
		}
		return "student_room";
	}

	@GetMapping("/complaints")
	public String viewComplaints(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent()) {
				List<Complaint> complaints = complaintService.getComplaintsByStudent(studentOpt.get());
				model.addAttribute("complaints", complaints != null ? complaints : new ArrayList<>());
			} else {
				model.addAttribute("complaints", new ArrayList<>());
			}
		} else {
			model.addAttribute("complaints", new ArrayList<>());
		}
		return "student_complaints";
	}

	@GetMapping("/complaints/new")
	public String newComplaintForm(Model model) {
		model.addAttribute("complaint", new Complaint());
		return "student_complaint_form";
	}

	@PostMapping("/complaints")
	public String submitComplaint(Authentication authentication, 
								  @RequestParam String subject,
								  @RequestParam String description,
								  @RequestParam String category) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent()) {
				Complaint complaint = new Complaint();
				complaint.setStudent(studentOpt.get());
				complaint.setSubject(subject);
				complaint.setDescription(description);
				complaint.setCategory(category);
				complaintService.saveComplaint(complaint);
			}
		}
		return "redirect:/student/complaints";
	}

	@GetMapping("/attendance")
	public String viewAttendance(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent()) {
				List<Attendance> attendance = attendanceService.getAttendanceByStudent(studentOpt.get());
				model.addAttribute("attendance", attendance != null ? attendance : new ArrayList<>());
			} else {
				model.addAttribute("attendance", new ArrayList<>());
			}
		} else {
			model.addAttribute("attendance", new ArrayList<>());
		}
		return "student_attendance";
	}

	@GetMapping("/payments")
	public String viewPayments(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent()) {
				List<Payment> payments = paymentService.getPaymentsByStudent(studentOpt.get());
				model.addAttribute("payments", payments != null ? payments : new ArrayList<>());
			} else {
				model.addAttribute("payments", new ArrayList<>());
			}
		} else {
			model.addAttribute("payments", new ArrayList<>());
		}
		return "student_payments";
	}

	@GetMapping("/mess-menu")
	public String viewMessMenu(Model model) {
		List<MessMenu> menus = messMenuService.getAllMenus();
		model.addAttribute("menus", menus != null ? menus : new ArrayList<>());
		return "student_mess_menu";
	}
}

