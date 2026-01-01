package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.Room;
import com.SmartHostel.repository.RoomRepository;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	public List<Room> getRoomsByHostel(String hostelName) {
		return roomRepository.findByHostelName(hostelName);
	}

	public List<Room> getAvailableRooms() {
		return roomRepository.findByAvailable(true);
	}

	public Optional<Room> getRoomById(Long id) {
		return roomRepository.findById(id);
	}

	public Optional<Room> getRoomByNumber(String roomNumber) {
		return roomRepository.findByRoomNumber(roomNumber);
	}

	public Room saveRoom(Room room) {
		return roomRepository.save(room);
	}

	public void deleteRoom(Long id) {
		roomRepository.deleteById(id);
	}
}

