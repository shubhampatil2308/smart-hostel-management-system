package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.Document;
import com.SmartHostel.model.Student;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
	List<Document> findByStudent(Student student);
	List<Document> findByStatus(String status);
}
