package com.example.portfolio.access.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
	@GetMapping("/")
	public String home() {
		return "redirect:/access/login"; // 기본 URL 접속 시 로그인 페이지로 리다이렉트
	}
	
}
