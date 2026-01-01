package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.Rating;
import com.SmartHostel.model.Hostel;
import com.SmartHostel.repository.RatingRepository;
import com.SmartHostel.repository.HostelRepository;
import java.util.List;

@Service
public class RatingService {

	@Autowired
	private RatingRepository ratingRepository;

	@Autowired
	private HostelRepository hostelRepository;

	public Rating saveRating(Rating rating) {
		Rating savedRating = ratingRepository.save(rating);
		updateHostelRating(rating.getHostel());
		return savedRating;
	}

	public List<Rating> getRatingsByHostel(Hostel hostel) {
		return ratingRepository.findByHostel(hostel);
	}

	private void updateHostelRating(Hostel hostel) {
		List<Rating> ratings = ratingRepository.findByHostel(hostel);
		if (!ratings.isEmpty()) {
			double average = ratings.stream()
				.mapToInt(Rating::getRating)
				.average()
				.orElse(0.0);
			hostel.setAverageRating(average);
			hostel.setTotalRatings(ratings.size());
			hostelRepository.save(hostel);
		}
	}
}
