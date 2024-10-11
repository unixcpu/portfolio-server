package com.example.portfolio.projects.controller;

import com.example.portfolio.access.model.Role;
import com.example.portfolio.access.model.User;
import com.example.portfolio.projects.model.Task;
import com.example.portfolio.projects.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class DashBoardApiController {
	@Autowired
	private TaskService taskService;
	
	@GetMapping
	public Map<String, Object> getDashboardData(Authentication authentication) {
		// 대시보드에 표시할 데이터를 가져오는 로직
		int totalTasks = taskService.getTotalTaskCount();
		int completedTasks = taskService.getCompletedTaskCount();
		int incompleteTasks = taskService.getIncompleteTaskCount();
		int overdueTasks = taskService.getOverdueTaskCount();
		
		// 작업 목록 가져오기
		List<Task> tasks = taskService.findAllTasks();
		
		// 응답 데이터 생성
		Map<String, Object> response = new HashMap<>();
		response.put("totalTasks", totalTasks);
		response.put("completedTasks", completedTasks);
		response.put("incompleteTasks", incompleteTasks);
		response.put("overdueTasks", overdueTasks);
		response.put("tasks", tasks);
		
		return response; // JSON 형식으로 응답
	}
}
