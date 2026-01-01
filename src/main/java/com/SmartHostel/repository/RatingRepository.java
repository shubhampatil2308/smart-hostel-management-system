package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.Rating;
import com.SmartHostel.model.Hostel;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
	List<Rating> findByHostel(Hostel hostel);
	List<Rating> findByCategory(String category);
}
