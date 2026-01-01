package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.SmartHostel.model.Document;
import com.SmartHostel.model.Student;
import com.SmartHostel.repository.DocumentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

	@Autowired
	private DocumentRepository documentRepository;

	private static final String UPLOAD_DIR = "uploads/documents/";

	public Document uploadDocument(Student student, MultipartFile file, String fileType) throws IOException {
		// Create upload directory if it doesn't exist
		Path uploadPath = Paths.get(UPLOAD_DIR);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		// Generate unique filename
		String originalFilename = file.getOriginalFilename();
		String extension = originalFilename != null && originalFilename.contains(".") 
			? originalFilename.substring(originalFilename.lastIndexOf(".")) 
			: "";
		String uniqueFilename = UUID.randomUUID().toString() + extension;

		// Save file
		Path filePath = uploadPath.resolve(uniqueFilename);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// Create document record
		Document document = new Document();
		document.setStudent(student);
		document.setFileName(originalFilename);
		document.setFilePath(filePath.toString());
		document.setFileType(fileType);
		document.setFileSize(file.getSize());
		document.setStatus("PENDING");

		return documentRepository.save(document);
	}

	public List<Document> getDocumentsByStudent(Student student) {
		return documentRepository.findByStudent(student);
	}

	public List<Document> getDocumentsByStatus(String status) {
		return documentRepository.findByStatus(status);
	}

	public void approveDocument(Long documentId, String remarks) {
		documentRepository.findById(documentId).ifPresent(document -> {
			document.setStatus("APPROVED");
			document.setRemarks(remarks);
			document.setReviewedAt(java.time.LocalDateTime.now());
			documentRepository.save(document);
		});
	}

	public void rejectDocument(Long documentId, String remarks) {
		documentRepository.findById(documentId).ifPresent(document -> {
			document.setStatus("REJECTED");
			document.setRemarks(remarks);
			document.setReviewedAt(java.time.LocalDateTime.now());
			documentRepository.save(document);
		});
	}
}
