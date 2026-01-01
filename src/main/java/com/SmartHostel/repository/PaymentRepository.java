package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.Payment;
import com.SmartHostel.model.Student;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByStudent(Student student);
	List<Payment> findByStatus(String status);
}

