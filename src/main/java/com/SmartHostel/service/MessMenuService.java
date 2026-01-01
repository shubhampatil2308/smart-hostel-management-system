package com.SmartHostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.SmartHostel.model.MessMenu;
import com.SmartHostel.repository.MessMenuRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MessMenuService {

	@Autowired
	private MessMenuRepository messMenuRepository;

	public List<MessMenu> getAllMenus() {
		return messMenuRepository.findAll();
	}

	public Optional<MessMenu> getMenuByDate(LocalDate date) {
		return messMenuRepository.findByDate(date);
	}

	public MessMenu saveMenu(MessMenu menu) {
		return messMenuRepository.save(menu);
	}

	public void deleteMenu(Long id) {
		messMenuRepository.deleteById(id);
	}
}

