package com.example.portfolio.projects.controller;

import com.example.portfolio.access.model.Role;
import com.example.portfolio.access.model.User;
import com.example.portfolio.access.service.UserService;
import com.example.portfolio.projects.model.Task;
import com.example.portfolio.projects.service.ProjectService;
import com.example.portfolio.projects.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class KanbanController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService; // Assuming you have a UserService to get users
	
	@GetMapping("/{id}/kanban")
	public String getKanbanBoard(@PathVariable("id") Long id, Model model, Authentication authentication) {
		String username = authentication.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		
		Role userRole = userService.findRoleByUsername(username);
		model.addAttribute("role", userRole.getName());
		
		List<Task> tasks = taskService.getTasksByProjectId(id);
		
		List<Task> todoTasks = new ArrayList<>();  // 할 일 리스트 초기화
		List<Task> inProgressTasks = new ArrayList<>();  // 진행 중 리스트 초기화
		List<Task> doneTasks = new ArrayList<>();  // 완료 리스트 초기화
		
		for (Task task : tasks) {
			String status = task.getStatus();
			
			if (status.equals("todo")) {
				todoTasks.add(task);  // 리스트에 추가
			} else if (status.equals("inprogress")) {
				inProgressTasks.add(task);  // 리스트에 추가
			} else if (status.equals("done")) {
				doneTasks.add(task);  // 리스트에 추가
			}
		}
		
		model.addAttribute("todoTasks", todoTasks);
		model.addAttribute("inProgressTasks", inProgressTasks);
		model.addAttribute("doneTasks", doneTasks);
		
		return "projects/kanban";
	}

}
