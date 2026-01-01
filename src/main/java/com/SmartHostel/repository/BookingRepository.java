package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.Booking;
import com.SmartHostel.model.Student;
import com.SmartHostel.model.Hostel;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByStudent(Student student);
	List<Booking> findByHostel(Hostel hostel);
	List<Booking> findByStatus(String status);
}

