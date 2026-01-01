package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.Payment;
import com.SmartHostel.model.Student;
import com.SmartHostel.repository.PaymentRepository;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	public List<Payment> getPaymentsByStudent(Student student) {
		return paymentRepository.findByStudent(student);
	}

	public List<Payment> getAllPayments() {
		return paymentRepository.findAll();
	}

	public List<Payment> getPaymentsByStatus(String status) {
		return paymentRepository.findByStatus(status);
	}

	public Payment savePayment(Payment payment) {
		return paymentRepository.save(payment);
	}

	public Optional<Payment> getPaymentById(Long id) {
		return paymentRepository.findById(id);
	}
}

