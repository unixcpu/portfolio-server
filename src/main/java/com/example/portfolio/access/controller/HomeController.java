package com.example.portfolio.access.controller;

import com.example.portfolio.access.model.Role;
import com.example.portfolio.access.model.User;
import com.example.portfolio.access.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@Autowired
	UserService userService;
	@GetMapping("/")
	public String home() {
		return "redirect:/access/login"; // 기본 URL 접속 시 로그인 페이지로 리다이렉트
	}
	@GetMapping("/access/home")
	public String homePage(Model model, Authentication authentication) {
		// 현재 인증된 사용자 이름 가져오기
		String currentUsername = authentication.getName();
		// 사용자 정보를 데이터베이스에서 조회
		User currentUser = userService.findByUsername(currentUsername);
		// 조회된 사용자 정보를 모델에 추가
		model.addAttribute("user", currentUser);
		Role userRole = userService.findRoleByUsername(currentUsername);
		model.addAttribute("role", userRole.getName());
		
		// home.html을 반환
		return "access/home";
	}

}
