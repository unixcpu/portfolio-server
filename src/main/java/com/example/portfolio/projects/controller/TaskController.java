package com.example.portfolio.projects.controller;

import com.example.portfolio.access.model.Role;
import com.example.portfolio.access.model.User;
import com.example.portfolio.access.service.UserService;
import com.example.portfolio.projects.model.Project;
import com.example.portfolio.projects.model.Task;
import com.example.portfolio.projects.service.ProjectService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.portfolio.projects.service.TaskService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projects")
public class TaskController {
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/{id}/tasks")
	public String createTaskForm(Authentication authentication, @PathVariable("id") Long id, Model model) {
		// 프로젝트의 마감일을 가져와서 모델에 추가
		Project project = projectService.findById(id); // 프로젝트 정보를 가져오는 서비스 메서드
		model.addAttribute("projectDueDate", project.getDueDate()); // 프로젝트 마감일을 추가
		
		String username = authentication.getName();
		User user = userService.findByUsername(username);
		Role userRole = userService.findRoleByUsername(username);
		
		model.addAttribute("role", userRole.getName());
		model.addAttribute("user", user);
		model.addAttribute("projectId", id);
		
		// 태스크 목록을 다시 조회하여 모델에 추가
		List<Task> tasks = taskService.getTasksByProjectId(id);
		model.addAttribute("tasks", tasks);
		
		return "projects/createTask"; // createTask.html로 이동
	}
	
	@PostMapping("/{id}/tasks")
	public String createTask(@NotNull Authentication authentication, @PathVariable("id") Long id, @NotNull @ModelAttribute Task task, @NotNull Model model) {
		String username = authentication.getName();
		Role userRole = userService.findRoleByUsername(username);
		model.addAttribute("role", userRole.getName());
		
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		
		task.setId(null); // ID 초기화하여 새로운 태스크로 인식되도록 함
		taskService.createTask(id, task);
		model.addAttribute("projectId", id);

		// 태스크 목록을 다시 조회하여 모델에 추가
		List<Task> tasks = taskService.getTasksByProjectId(id);
		model.addAttribute("tasks", tasks);
		
		return "redirect:/projects/" + id + "/tasks"; // GET 요청으로 리다이렉트
	}
	@PostMapping("/tasks/delete")
	public String deleteTask(@RequestParam Long id, @RequestParam Long projectId) {
		taskService.deleteTask(id);
		// 프로젝트 ID를 사용하여 추가적인 로직을 구현할 수 있습니다.
		return "redirect:/projects/" + projectId + "/tasks"; // GET 요청으로 리다이렉트
	}

}

