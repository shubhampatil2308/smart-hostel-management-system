package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.Hostel;
import com.SmartHostel.model.User;
import com.SmartHostel.repository.HostelRepository;
import java.util.List;
import java.util.Optional;

@Service
public class HostelService {

	@Autowired
	private HostelRepository hostelRepository;

	public List<Hostel> getAllHostels() {
		return hostelRepository.findAll();
	}

	public List<Hostel> getHostelsByWarden(User warden) {
		return hostelRepository.findByWarden(warden);
	}

	public List<Hostel> getHostelsByStatus(String status) {
		return hostelRepository.findByStatus(status);
	}

	public List<Hostel> getHostelsByCity(String city) {
		return hostelRepository.findByCity(city);
	}

	public Optional<Hostel> getHostelById(Long id) {
		return hostelRepository.findById(id);
	}

	public Hostel saveHostel(Hostel hostel) {
		return hostelRepository.save(hostel);
	}

	public void deleteHostel(Long id) {
		hostelRepository.deleteById(id);
	}
}
