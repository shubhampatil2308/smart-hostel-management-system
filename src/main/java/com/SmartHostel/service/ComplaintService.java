package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.Complaint;
import com.SmartHostel.model.Student;
import com.SmartHostel.repository.ComplaintRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

	@Autowired
	private ComplaintRepository complaintRepository;

	@Autowired
	private EmailService emailService;

	public List<Complaint> getComplaintsByStudent(Student student) {
		return complaintRepository.findByStudent(student);
	}

	public List<Complaint> getAllComplaints() {
		return complaintRepository.findAll();
	}

	public List<Complaint> getComplaintsByStatus(String status) {
		return complaintRepository.findByStatus(status);
	}

	public Complaint saveComplaint(Complaint complaint) {
		return complaintRepository.save(complaint);
	}

	public Complaint updateComplaintStatus(Long id, String status, String response) {
		Complaint complaint = complaintRepository.findById(id).orElse(null);
		if (complaint != null) {
			complaint.setStatus(status);
			complaint.setResponse(response);
			if (status.equals("RESOLVED")) {
				complaint.setResolvedAt(LocalDateTime.now());
				// Send email notification
				emailService.sendComplaintResponseEmail(
					complaint.getStudent().getUser().getEmail(),
					complaint.getStudent().getUser().getUsername(),
					complaint.getSubject(),
					response
				);
			}
			return complaintRepository.save(complaint);
		}
		return null;
	}

	public Optional<Complaint> getComplaintById(Long id) {
		return complaintRepository.findById(id);
	}
}

