package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.AuditLog;
import com.SmartHostel.model.User;
import com.SmartHostel.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuditService {

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
		
		if (request != null) {
			auditLog.setIpAddress(getClientIpAddress(request));
			auditLog.setUserAgent(request.getHeader("User-Agent"));
		}
		
		auditLogRepository.save(auditLog);
	}

	public List<AuditLog> getAuditLogsByUser(User user) {
		return auditLogRepository.findByUser(user);
	}

	public List<AuditLog> getAuditLogsByEntity(String entityType, Long entityId) {
		return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
	}

	private String getClientIpAddress(HttpServletRequest request) {
		String xForwardedFor = request.getHeader("X-Forwarded-For");
		if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
			return xForwardedFor.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}
}

