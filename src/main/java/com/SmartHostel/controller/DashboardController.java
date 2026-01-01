package com.SmartHostel.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication) {
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
			return "redirect:/admin/dashboard";
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("WARDEN"))) {
			return "redirect:/warden/dashboard";
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("STUDENT"))) {
			return "redirect:/student/dashboard";
		}
		return "redirect:/login";
	}
}

