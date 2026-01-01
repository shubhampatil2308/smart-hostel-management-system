package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SmartHostel.model.User;
import com.SmartHostel.model.Student;
import com.SmartHostel.repository.UserRepository;
import com.SmartHostel.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Transactional
    public User register(User user) {
        // Hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Save user
        User savedUser = userRepository.save(user);
        
        // If student, create student record
        if (savedUser.getRole().name().equals("STUDENT")) {
            Student student = new Student();
            student.setUser(savedUser);
            student.setAdmissionDate(LocalDate.now());
            student.setStatus("ACTIVE");
            studentRepository.save(student);
        }
        
        // Send registration email
        emailService.sendRegistrationEmail(
            savedUser.getEmail(), 
            savedUser.getUsername(), 
            savedUser.getRole().name()
        );
        
        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
