package com.example.portfolio.access.service;

import com.example.portfolio.access.repo.RoleRepository;
import com.example.portfolio.access.model.Role;
import com.example.portfolio.access.model.User;
import com.example.portfolio.access.repo.UserRepository;
import com.example.portfolio.access.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAccountInitializer implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		// 관리자가 이미 존재하는지 확인
		if (!userRepository.existsByUsername("admin")) {
			// 관리자 역할(Role)을 찾거나 없으면 생성
			Role adminRole = roleRepository.findByName("ADMIN")
					                 .orElseGet(() -> {
						                 Role newRole = new Role();
						                 newRole.setName("ADMIN");
						                 return roleRepository.save(newRole); // 역할을 데이터베이스에 저장
					                 });
			
			// 'USER' 역할(Role)을 찾거나 없으면 생성
			Role userRole = roleRepository.findByName("USER")
					                .orElseGet(() -> {
						                Role newRole = new Role();
						                newRole.setId(2L); // ID를 명시적으로 설정
						                newRole.setName("USER");
						                return roleRepository.save(newRole); // 역할을 데이터베이스에 저장
					                });
			
			// 관리자 계정 생성
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin123")); // 비밀번호 암호화
			
			// UserRole 객체 생성 및 설정
			UserRole userRoleAdmin = new UserRole();
			userRoleAdmin.setUser(admin);
			userRoleAdmin.setRole(adminRole);
			
			// UserRole을 User의 역할 목록에 추가
			admin.getUserRoles().add(userRoleAdmin);
			
			// Role 객체에도 UserRole 추가
			adminRole.getUserRoles().add(userRoleAdmin);
			
			// 사용자와 역할 정보 저장
			userRepository.save(admin);
			roleRepository.save(adminRole);
			roleRepository.save(userRole); // 'USER' 역할 저장
			
			// 이미 사용자 저장은 이전에 했으므로 여기서 중복 저장하지 않도록 주의
		}
	}

}
