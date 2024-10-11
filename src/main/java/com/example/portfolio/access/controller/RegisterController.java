package com.example.portfolio.access.controller;

import com.example.portfolio.access.service.UserService;
import com.example.portfolio.access.model.User;
import com.example.portfolio.access.service.UsernameAlreadyExistsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/access")
public class RegisterController {
	
	private final UserService userService;
	
	public RegisterController(UserService userService) {
		this.userService = userService;
	}
	
	// 사용자 등록 폼을 보여주는 메서드
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User()); // 빈 User 객체를 모델에 추가
		return "access/register"; // Thymeleaf 템플릿 이름
	}
	
	// 사용자 등록 처리 메서드
	@PostMapping("/register")
	public String registerUser(@RequestParam String username,
	                           @RequestParam String password, Model model) {
		// 사용자 생성 및 등록 로직
		try {
			userService.registerUser(username, password);
			return "redirect:/access/login"; // 성공 페이지로 리다이렉트
		} catch (UsernameAlreadyExistsException e) {
			model.addAttribute("error", e.getMessage()); // 오류 메시지 설정
			return "access/login"; // 등록 페이지로 리다이렉트
		}
	}
}
