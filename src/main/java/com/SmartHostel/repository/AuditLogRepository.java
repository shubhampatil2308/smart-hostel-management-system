package com.SmartHostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.SmartHostel.model.AuditLog;
import com.SmartHostel.model.User;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	List<AuditLog> findByUser(User user);
	List<AuditLog> findByEntityType(String entityType);
	List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
	List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
