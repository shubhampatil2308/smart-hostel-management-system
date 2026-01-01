package com.SmartHostel.controller;

import com.SmartHostel.model.User;
import com.SmartHostel.model.Role;
import com.SmartHostel.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Show Register Form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("user", new User());
        return "register";
    }

    // Handle Register
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String phone,
                               @RequestParam Role role,
                               @RequestParam(required = false) String course,
                               @RequestParam(required = false) String department,
                               @RequestParam(required = false) String studentId,
                               @RequestParam(required = false) String hostelName,
                               Model model) {
        Optional<User> existingUser = userService.findByEmail(email);
        if (existingUser.isPresent()) {
            model.addAttribute("error", "Email already registered!");
            model.addAttribute("roles", Role.values());
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(role);

        // Set role-specific fields
        if (role == Role.STUDENT) {
            user.setCourse(course);
            user.setDepartment(department);
            user.setStudentId(studentId);
        } else if (role == Role.WARDEN) {
            user.setHostelName(hostelName);
        }

        try {
            userService.register(user);
            model.addAttribute("success", "Registration successful! Please check your email for confirmation.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("roles", Role.values());
            return "register";
        }
    }

    // Show Login Form
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password!");
        }
        return "login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
}
