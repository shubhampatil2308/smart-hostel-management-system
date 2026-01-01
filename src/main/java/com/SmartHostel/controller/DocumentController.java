package com.SmartHostel.controller;

import com.SmartHostel.model.*;
import com.SmartHostel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class DocumentController {

	@Autowired
	private UserService userService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private DocumentService documentService;

	@GetMapping("/documents")
	public String viewDocuments(Authentication authentication, Model model) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent()) {
				List<Document> documents = documentService.getDocumentsByStudent(studentOpt.get());
				model.addAttribute("documents", documents != null ? documents : new java.util.ArrayList<>());
			}
		}
		return "student_documents";
	}

	@PostMapping("/documents/upload")
	public String uploadDocument(Authentication authentication,
								@RequestParam MultipartFile file,
								@RequestParam String fileType) {
		Optional<User> userOpt = userService.findByEmail(authentication.getName());
		if (userOpt.isPresent()) {
			Optional<Student> studentOpt = studentService.getStudentByUser(userOpt.get());
			if (studentOpt.isPresent() && !file.isEmpty()) {
				try {
					documentService.uploadDocument(studentOpt.get(), file, fileType);
					return "redirect:/student/documents?success=true";
				} catch (IOException e) {
					return "redirect:/student/documents?error=upload_failed";
				}
			}
		}
		return "redirect:/student/documents?error=true";
	}
}

