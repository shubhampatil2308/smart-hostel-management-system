package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.Attendance;
import com.SmartHostel.model.Student;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	List<Attendance> findByStudent(Student student);
	List<Attendance> findByDate(LocalDate date);
	List<Attendance> findByStudentAndDate(Student student, LocalDate date);
}

