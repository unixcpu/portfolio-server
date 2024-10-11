package com.example.portfolio.access.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/access")
public class LoginController {
	
	@GetMapping("/login")
	public String login() {
		return "access/login"; // login.html을 반환
	}
	
	@PostMapping("/logout")
	public String logout() {
		return "redirect:/access/login";
	}
	
}
