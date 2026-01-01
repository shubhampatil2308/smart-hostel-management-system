package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.Hostel;
import com.SmartHostel.model.User;
import java.util.List;
import java.util.Optional;

public interface HostelRepository extends JpaRepository<Hostel, Long> {
	Optional<Hostel> findByName(String name);
	List<Hostel> findByWarden(User warden);
	List<Hostel> findByStatus(String status);
	List<Hostel> findByCity(String city);
}
