package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.Student;
import com.SmartHostel.model.User;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
	Optional<Student> findByUser(User user);
	List<Student> findByStatus(String status);
}

