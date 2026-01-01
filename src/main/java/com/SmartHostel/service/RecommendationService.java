package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.Hostel;
import com.SmartHostel.model.Student;
import com.SmartHostel.repository.HostelRepository;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

	@Autowired
	private HostelRepository hostelRepository;

	@Autowired
	private RatingService ratingService;

	public List<Hostel> getRecommendedHostels(Student student, String preferences) {
		List<Hostel> allHostels = hostelRepository.findByStatus("ACTIVE");
		
		// Parse preferences (JSON string: {"city": "Mumbai", "roomType": "Single", "maxRent": 5000})
		Map<String, String> prefs = parsePreferences(preferences);
		
		// Score each hostel based on preferences
		Map<Hostel, Double> scores = new HashMap<>();
		
		for (Hostel hostel : allHostels) {
			double score = 0.0;
			
			// City match (40% weight)
			if (prefs.containsKey("city") && hostel.getCity() != null) {
				if (hostel.getCity().equalsIgnoreCase(prefs.get("city"))) {
					score += 0.4;
				}
			}
			
			// Rating (30% weight)
			if (hostel.getAverageRating() != null) {
				score += (hostel.getAverageRating() / 5.0) * 0.3;
			}
			
			// Availability (20% weight)
			if (hostel.getAvailableRooms() != null && hostel.getTotalRooms() != null) {
				double availability = (double) hostel.getAvailableRooms() / hostel.getTotalRooms();
				score += availability * 0.2;
			}
			
			// Price match (10% weight) - if maxRent specified
			if (prefs.containsKey("maxRent") && hostel.getRooms() != null) {
				Optional<Double> minRent = hostel.getRooms().stream()
					.map(room -> room.getRent())
					.filter(rent -> rent != null)
					.min(Double::compare);
				
				if (minRent.isPresent()) {
					double maxRent = Double.parseDouble(prefs.get("maxRent"));
					if (minRent.get() <= maxRent) {
						score += 0.1;
					}
				}
			}
			
			scores.put(hostel, score);
		}
		
		// Sort by score and return top 10
		return scores.entrySet().stream()
			.sorted(Map.Entry.<Hostel, Double>comparingByValue().reversed())
			.limit(10)
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
	}

	private Map<String, String> parsePreferences(String preferences) {
		Map<String, String> prefs = new HashMap<>();
		if (preferences != null && !preferences.isEmpty()) {
			try {
				// Simple JSON parsing (for production, use Gson or Jackson)
				preferences = preferences.replace("{", "").replace("}", "").replace("\"", "");
				String[] pairs = preferences.split(",");
				for (String pair : pairs) {
					String[] keyValue = pair.split(":");
					if (keyValue.length == 2) {
						prefs.put(keyValue[0].trim(), keyValue[1].trim());
					}
				}
			} catch (Exception e) {
				// Handle parsing error
			}
		}
		return prefs;
	}
}
