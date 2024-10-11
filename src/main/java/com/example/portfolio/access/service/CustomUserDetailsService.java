package com.example.portfolio.access.service;

import com.example.portfolio.access.repo.UserRepository;
import com.example.portfolio.access.model.User;
import com.example.portfolio.access.model.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@Transactional // 이 어노테이션을 추가하여 트랜잭션 범위 내에서 데이터 접근
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		List<UserRole> roles = user.getUserRoles();
		if (roles != null) { // null 체크 추가
			for (UserRole role : roles) {
				authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
			}
		}
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
}

