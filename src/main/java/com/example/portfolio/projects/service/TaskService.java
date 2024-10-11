package com.example.portfolio.projects.service;

import com.example.portfolio.projects.model.Project;
import com.example.portfolio.projects.model.Task;
import jakarta.transaction.Transactional;

import java.util.List;

public interface TaskService {
	
	List<Task> findByProject(Project project);
	
	Project createProject(Project project);

	List<Task> getTasksByProjectId(Long id);
	
	Task findById(Long taskId); // ID로 태스크 조회 메소드 추가
	
	void createTask(Long id, Task task);
	
	@Transactional
	void updateTaskPriority(Long taskId, Integer priority);
	
	void deleteTask(Long taskId);
	
	void updateTaskStatus(Long id, String status);
	
	int getTotalTaskCount();
	
	int getCompletedTaskCount();
	
	int getIncompleteTaskCount();
	
	int getOverdueTaskCount();
	
	List<Task> findAllTasks();
}