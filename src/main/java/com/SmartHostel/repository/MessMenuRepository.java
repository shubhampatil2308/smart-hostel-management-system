package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.MessMenu;
import java.time.LocalDate;
import java.util.Optional;

public interface MessMenuRepository extends JpaRepository<MessMenu, Long> {
	Optional<MessMenu> findByDate(LocalDate date);
}

