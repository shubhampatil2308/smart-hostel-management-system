package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.AuditLog;
import com.SmartHostel.model.User;
import com.SmartHostel.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

	@Autowired
	private AuditLogRepository auditLogRepository;

	public void logAction(User user, String action, String entityType, Long entityId, 
						  String oldValue, String newValue, HttpServletRequest request) {
		AuditLog auditLog = new AuditLog();
		auditLog.setUser(user);
		auditLog.setAction(action);
		auditLog.setEntityType(entityType);
		auditLog.setEntityId(entityId);
		auditLog.setOldValue(oldValue);
		auditLog.setNewValue(newValue);
		auditLog.setTimestamp(LocalDateTime.now());
		
		if (request != null) {
			auditLog.setIpAddress(getClientIpAddress(request));
			auditLog.setUserAgent(request.getHeader("User-Agent"));
		}
		
		auditLogRepository.save(auditLog);
	}

	public List<AuditLog> getLogsByUser(User user) {
		return auditLogRepository.findByUser(user);
	}

	public List<AuditLog> getLogsByEntityType(String entityType) {
		return auditLogRepository.findByEntityType(entityType);
	}

	public List<AuditLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end) {
		return auditLogRepository.findByTimestampBetween(start, end);
	}

	private String getClientIpAddress(HttpServletRequest request) {
		String xForwardedFor = request.getHeader("X-Forwarded-For");
		if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
			return xForwardedFor.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}
}

