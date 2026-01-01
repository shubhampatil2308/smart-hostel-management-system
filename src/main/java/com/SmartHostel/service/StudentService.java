package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.Student;
import com.SmartHostel.model.User;
import com.SmartHostel.repository.StudentRepository;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	public Optional<Student> getStudentByUser(User user) {
		return studentRepository.findByUser(user);
	}

	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public List<Student> getStudentsByStatus(String status) {
		return studentRepository.findByStatus(status);
	}

	public Student saveStudent(Student student) {
		return studentRepository.save(student);
	}

	public Optional<Student> getStudentById(Long id) {
		return studentRepository.findById(id);
	}
}

