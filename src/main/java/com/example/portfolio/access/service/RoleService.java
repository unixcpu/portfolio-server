package com.example.portfolio.access.service;

import com.example.portfolio.access.repo.UserRepository;
import com.example.portfolio.access.repo.UserRoleRepository;
import com.example.portfolio.access.model.Role;
import com.example.portfolio.access.repo.RoleRepository;
import com.example.portfolio.access.model.User;
import com.example.portfolio.access.model.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RoleService {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	public RoleService(RoleRepository roleRepository, UserRepository userRepository, UserRoleRepository userRoleRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
	}
	
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}
	@Transactional
	public boolean assignRoleToUser(Long userId, String roleName) {
		// 사용자와 역할을 조회합니다.
		User user = userRepository.findById(userId)
				            .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
		Role role = roleRepository.findByName(roleName)
				            .orElseThrow(() -> new NoSuchElementException("Role not found with name: " + roleName));
		
		// 기존의 UserRole을 조회합니다.
		Optional<UserRole> existingUserRole = userRoleRepository.findFirstByUserIdAndRoleId(user.getId(), role.getId());
		
		if (existingUserRole.isPresent() && !existingUserRole.get().getRemoved()) {
			// 로직 처리
			return true;
		}
		
		// 중복이 없는 경우 기존의 UserRole들을 비활성화합니다.
		List<UserRole> existingRoles = userRoleRepository.findByUserId(user.getId());
		for (UserRole ur : existingRoles) {
			ur.setRemoved(true);
			userRoleRepository.save(ur);
		}
		
		// 새로운 UserRole을 생성하고 사용자 및 역할을 설정합니다.
		UserRole newUserRole = new UserRole();
		newUserRole.setUser(user);
		newUserRole.setRole(role);
		newUserRole.setRemoved(false);
		
		// 사용자 역할 목록과 역할의 UserRoles 목록에 새 UserRole을 추가합니다.
		user.getUserRoles().add(newUserRole);
		role.getUserRoles().add(newUserRole);
		
		// 데이터베이스에 저장합니다.
		userRoleRepository.save(newUserRole);
		
		return true;
	}
}
