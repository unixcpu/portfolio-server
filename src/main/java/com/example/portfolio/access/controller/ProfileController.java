package com.example.portfolio.access.controller;

import com.example.portfolio.access.model.Role;
import com.example.portfolio.access.model.User;
import com.example.portfolio.access.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/access")
public class ProfileController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/profile")
	public String showProfilePage(Model model, Authentication authentication) {
		String username = authentication.getName();
		User user = userService.findByUsername(username);
		if (user != null) {
			model.addAttribute("user", user);
		} else {
			model.addAttribute("user", new User()); // 기본 User 객체 설정 (필요시)
		}
		Role userRole = userService.findRoleByUsername(username);
		model.addAttribute("role", userRole.getName());
		return "access/profile"; // profile.html 반환
	}
	
	@PostMapping("/profile/update")
	public String updateProfile(@RequestParam("username") String username,
	                            @RequestParam("email") String email,
	                            @RequestParam("password") String password,
	                            @RequestParam("profilePic") MultipartFile profilePic,
	                            Authentication authentication, Model model) {
		try {
			Role userRole = userService.findRoleByUsername(username);
			model.addAttribute("role", userRole.getName());
			userService.updateUserProfile(authentication, username, email, password, profilePic);
			return "redirect:/access/profile?success=true";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "프로필 업데이트 중 오류가 발생했습니다.");
			return "access/profile"; // 오류 발생 시 다시 프로필 페이지로 이동
		}
	}

}
