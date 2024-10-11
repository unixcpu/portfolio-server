package com.example.portfolio.projects.controller;

import com.example.portfolio.projects.model.Task;
import com.example.portfolio.projects.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class TaskApiController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	
	@Autowired
	private TaskService taskService;
	
	@PostMapping("/tasklist")
	public ResponseEntity<Void> updateTaskList(@RequestBody List<Task> tasks) {
		for (Task task : tasks) {
			logger.info("Received task update request: {}", task); // 각 태스크 로그 남기기
			taskService.updateTaskPriority(task.getId(), task.getPriority());
			
			taskService.updateTaskStatus(task.getId(), task.getStatus());
		}
		
		logger.info("All tasks updated successfully"); // 모든 태스크 업데이트 성공 로그
		
		return ResponseEntity.ok().build(); // 성공적으로 처리한 경우
	}
	@PostMapping("/dragdrop")
	public ResponseEntity<Void> updateKanbanList(@RequestBody Task task) {
		logger.info("Received task update request: {}", task); // 각 태스크 로그 남기기
		//taskService.updateTaskPriority(task.getId(), task.getPriority());
		
		taskService.updateTaskStatus(task.getId(), task.getStatus());
		
		logger.info("All tasks updated successfully"); // 모든 태스크 업데이트 성공 로그
		
		return ResponseEntity.ok().build(); // 성공적으로 처리한 경우
	}
}