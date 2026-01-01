package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.Attendance;
import com.SmartHostel.model.Student;
import com.SmartHostel.repository.AttendanceRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	public List<Attendance> getAttendanceByStudent(Student student) {
		return attendanceRepository.findByStudent(student);
	}

	public List<Attendance> getAttendanceByDate(LocalDate date) {
		return attendanceRepository.findByDate(date);
	}

	public Attendance markAttendance(Student student, LocalDate date, LocalTime checkInTime, String status) {
		Attendance attendance = new Attendance();
		attendance.setStudent(student);
		attendance.setDate(date);
		attendance.setCheckInTime(checkInTime);
		attendance.setStatus(status);
		return attendanceRepository.save(attendance);
	}

	public Attendance updateCheckOut(Student student, LocalDate date, LocalTime checkOutTime) {
		List<Attendance> attendances = attendanceRepository.findByStudentAndDate(student, date);
		if (!attendances.isEmpty()) {
			Attendance attendance = attendances.get(0);
			attendance.setCheckOutTime(checkOutTime);
			return attendanceRepository.save(attendance);
		}
		return null;
	}

	public List<Attendance> getAllAttendance() {
		return attendanceRepository.findAll();
	}
}

