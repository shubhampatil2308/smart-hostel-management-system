package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.Room;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
	Optional<Room> findByRoomNumber(String roomNumber);
	List<Room> findByHostelName(String hostelName);
	List<Room> findByAvailable(boolean available);
}

