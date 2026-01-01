package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.Complaint;
import com.SmartHostel.model.Student;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
	List<Complaint> findByStudent(Student student);
	List<Complaint> findByStatus(String status);
}

