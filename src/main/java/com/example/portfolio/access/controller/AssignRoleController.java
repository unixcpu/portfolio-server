package com.example.portfolio.access.controller;

import com.example.portfolio.access.service.RoleService;
import com.example.portfolio.access.service.UserService;
import com.example.portfolio.access.model.Role;
import com.example.portfolio.access.model.User;
import com.example.portfolio.access.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/access")
public class AssignRoleController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping("/assign-role")
	public String getRoleAssignmentPage(Model model, Authentication authentication) {
		List<User> users = userService.getAllUsers();
		List<Role> availableRoles = roleService.getAllRoles(); // 전체 역할 목록 가져오기
		
		// 사용자 역할을 필터링하여 is_removed = 0인 역할만 포함
		Map<Long, List<UserRole>> userRolesMap = new HashMap<>();
		for (User user : users) {
			List<UserRole> activeRoles = user.getUserRoles().stream()
					                             .filter(userRole -> !userRole.getRemoved())
					                             .collect(Collectors.toList());
			userRolesMap.put(user.getId(), activeRoles);
		}
		
		String currentUsername = authentication.getName();
		User currentUser = userService.findByUsername(currentUsername);
		
		model.addAttribute("user", currentUser);
		model.addAttribute("users", users);
		model.addAttribute("userRolesMap", userRolesMap);
		model.addAttribute("availableRoles", availableRoles);
		Role userRole = userService.findRoleByUsername(currentUsername);
		model.addAttribute("role", userRole.getName());
		return "access/assign-role"; // 템플릿 이름 수정
	}

	@PostMapping("/assign-role")
	public String assignRole(@RequestParam Long userId, @RequestParam String roleName, Model model) {
		try {
			roleService.assignRoleToUser(userId, roleName);
		} catch (NoSuchElementException e) {
			model.addAttribute("error", "Role or User not found.");
			return "redirect:/access/assign-role"; // 오류 메시지를 전달할 페이지로 리디렉션
		}
		
		return "redirect:/access/assign-role";
	}

}
