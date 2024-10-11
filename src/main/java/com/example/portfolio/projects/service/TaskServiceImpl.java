package com.example.portfolio.projects.service;

import com.example.portfolio.projects.model.Project;
import com.example.portfolio.projects.model.Task;
import com.example.portfolio.projects.repo.ProjectRepository;
import com.example.portfolio.projects.repo.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;
	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository) {
		this.taskRepository = taskRepository;
		this.projectRepository = projectRepository;
	}
	
	@Override
	public List<Task> findByProject(Project project) {
		return taskRepository.findByProject(project); // 프로젝트 ID로 태스크 목록 조회
	}

	@Override
	public Project createProject(Project project) {
		Project savedProject = projectRepository.save(project);
		
		// 프로젝트 생성 시 기본 태스크 생성
		createDefaultTasks(savedProject.getId());
		
		return savedProject;
	}
	
	@Override
	public void createTask(Long projectId, Task task) {
		// 프로젝트 ID를 태스크에 설정
		Project project = projectRepository.findById(projectId)
				                  .orElseThrow(() -> new RuntimeException("Project not found"));
		
		task.setProject(project); // 프로젝트 설정
		
		// 태스크의 우선순위가 null인 경우 가장 낮은 우선순위로 설정
		if (task.getPriority() == null) {
			task.setPriority(getNextPriorityForProject(project)); // 다음 우선순위로 설정
		}
		taskRepository.save(task);
	}
	// 새로운 태스크를 추가할 때 현재 프로젝트의 최대 우선순위를 기준으로 다음 우선순위 반환
	private int getNextPriorityForProject(Project project) {
		List<Task> tasks = taskRepository.findByProjectOrderByPriorityAsc(project);
		if (tasks.isEmpty()) {
			return 1; // 태스크가 없으면 우선순위를 1로 설정
		}
		// 현재 최대 우선순위 + 1 반환
		return tasks.get(tasks.size() - 1).getPriority() + 1;
	}
	
	@Override
	public List<Task> getTasksByProjectId(Long projectId) {
		Project project = projectRepository.findById(projectId)
				                  .orElseThrow(() -> new RuntimeException("Project not found"));
		return taskRepository.findByProjectOrderByPriorityAsc(project); // 우선순위에 따라 정렬
	}
	
	private void createDefaultTasks(Long projectId) {
		// 프로젝트를 가져옵니다.
		Project project = projectRepository.findById(projectId)
				                  .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));
		
		// 기본 태스크 생성
		Task todoTask = new Task();
		todoTask.setName("기본 할 일");
		todoTask.setStatus("할 일");
		todoTask.setProject(project); // Project 객체 할당
		
		Task inProgressTask = new Task();
		inProgressTask.setName("진행 중 태스크");
		inProgressTask.setStatus("진행 중");
		inProgressTask.setProject(project); // Project 객체 할당
		
		Task doneTask = new Task();
		doneTask.setName("완료 태스크");
		doneTask.setStatus("완료");
		doneTask.setProject(project); // Project 객체 할당
		
		// 태스크 저장
		taskRepository.save(todoTask);
		taskRepository.save(inProgressTask);
		taskRepository.save(doneTask);
	}
	
	public Task findById(Long taskId) {
		// 리포지토리에서 태스크를 찾아 Optional로 반환
		return taskRepository.findById(taskId)
				       .orElseThrow(() -> new EntityNotFoundException("해당 태스크를 찾을 수 없습니다. ID: " + taskId));
	}
	@Override
	@Transactional
	public void updateTaskPriority(Long taskId, Integer priority) {
		int rowsUpdated = taskRepository.updateTaskPriorityById(taskId, priority);
		if (rowsUpdated == 0) {
			throw new RuntimeException("태스크 업데이트 실패: 태스크를 찾을 수 없습니다.");
		}
	}
	
	@Override
	public void deleteTask(Long taskId) {
		Task task = findById(taskId);
		taskRepository.delete(task);
	}
	
	@Override
	public void updateTaskStatus(Long id, String status) {
		Task task = findById(id);
		task.setStatus(status);
		taskRepository.save(task);
	}
	
}
