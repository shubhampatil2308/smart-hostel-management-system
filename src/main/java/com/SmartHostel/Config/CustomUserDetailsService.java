package com.SmartHostel.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.SmartHostel.model.User;
import com.SmartHostel.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userOpt = userService.findByEmail(email);
		if (userOpt.isEmpty() || !userOpt.get().isEnabled()) {
			throw new UsernameNotFoundException("User not found or disabled: " + email);
		}

		User hostelUser = userOpt.get();
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(hostelUser.getRole().name()));

		return org.springframework.security.core.userdetails.User
			.withUsername(hostelUser.getEmail())
			.password(hostelUser.getPassword())
			.authorities(authorities)
			.accountExpired(false)
			.accountLocked(false)
			.credentialsExpired(false)
			.disabled(!hostelUser.isEnabled())
			.build();
	}
}

