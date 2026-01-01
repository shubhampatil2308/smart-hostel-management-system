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
@RequestMapping("/warden")
public class HostelController {

	@Autowired
	private HostelService hostelService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoomService roomService;

	@GetMapping("/hostel/register")
	public String showHostelRegistrationForm(Model model) {
		model.addAttribute("hostel", new Hostel());
		return "warden_hostel_register";
	}

	@PostMapping("/hostel/register")
	public String registerHostel(Authentication authentication,
								  @RequestParam String name,
								  @RequestParam String address,
								  @RequestParam String city,
								  @RequestParam String state,
								  @RequestParam String pincode,
								  @RequestParam String phone,
								  @RequestParam String email,
								  @RequestParam String description,
								  @RequestParam String amenities,
								  @RequestParam Integer totalRooms) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent() && userOpt.get().getRole() == Role.WARDEN) {
			Hostel hostel = new Hostel();
			hostel.setName(name);
			hostel.setAddress(address);
			hostel.setCity(city);
			hostel.setState(state);
			hostel.setPincode(pincode);
			hostel.setPhone(phone);
			hostel.setEmail(email);
			hostel.setDescription(description);
			hostel.setAmenities(amenities);
			hostel.setTotalRooms(totalRooms);
			hostel.setAvailableRooms(totalRooms);
			hostel.setWarden(userOpt.get());
			hostel.setStatus("PENDING_APPROVAL");
			
			hostelService.saveHostel(hostel);
			return "redirect:/warden/dashboard?hostel_registered=true";
		}
		return "redirect:/warden/hostel/register?error=true";
	}

	@GetMapping("/hostels")
	public String viewHostels(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			List<Hostel> hostels = hostelService.getHostelsByWarden(userOpt.get());
			model.addAttribute("hostels", hostels);
		}
		return "warden_hostels";
	}
}
