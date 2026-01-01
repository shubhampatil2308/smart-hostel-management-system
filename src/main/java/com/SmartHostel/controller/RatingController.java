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
@RequestMapping("/student")
public class RatingController {

	@Autowired
	private UserService userService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private HostelService hostelService;

	@Autowired
	private RatingService ratingService;

	@GetMapping("/ratings")
	public String viewRatings(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent()) {
				// Get hostels student has booked
				List<Hostel> hostels = hostelService.getAllHostels();
				model.addAttribute("hostels", hostels);
			}
		}
		return "student_ratings";
	}

	@PostMapping("/ratings")
	public String submitRating(Authentication authentication,
							   @RequestParam Long hostelId,
							   @RequestParam Integer rating,
							   @RequestParam String review,
							   @RequestParam String category) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			Optional<Hostel> hostelOpt = hostelService.getHostelById(hostelId);
			
			if (studentOpt.isPresent() && hostelOpt.isPresent()) {
				Rating ratingObj = new Rating();
				ratingObj.setStudent(studentOpt.get());
				ratingObj.setHostel(hostelOpt.get());
				ratingObj.setRating(rating);
				ratingObj.setReview(review);
				ratingObj.setCategory(category);
				
				ratingService.saveRating(ratingObj);
				return "redirect:/student/ratings?success=true";
			}
		}
		return "redirect:/student/ratings?error=true";
	}
}
