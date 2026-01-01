package com.SmartHostel.controller;

import com.SmartHostel.model.User;
import com.SmartHostel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

	@Autowired
	private AnalyticsService analyticsService;

	@Autowired
	private UserService userService;

	@GetMapping("/dashboard")
	public Map<String, Object> getDashboardData(Authentication authentication) {
		User user = userService.findByEmail(authentication.getName()).orElse(null);
		if (user != null) {
			return analyticsService.getDashboardAnalytics(user.getRole().name(), user);
		}
		return Map.of();
	}

	@GetMapping("/booking-trends")
	public Map<String, Long> getBookingTrends(@RequestParam(defaultValue = "30") int days) {
		return analyticsService.getBookingTrends(days);
	}

	@GetMapping("/revenue-trends")
	public Map<String, Double> getRevenueTrends(@RequestParam(defaultValue = "30") int days) {
		return analyticsService.getRevenueTrends(days);
	}

	@GetMapping("/complaint-distribution")
	public Map<String, Long> getComplaintDistribution() {
		return analyticsService.getComplaintStatusDistribution();
	}
}
