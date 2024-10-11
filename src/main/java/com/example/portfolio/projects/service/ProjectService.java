package com.example.portfolio.projects.service;

import com.example.portfolio.access.model.User;
import com.example.portfolio.projects.model.Project;
import com.example.portfolio.projects.model.ProjectUser;
import com.example.portfolio.projects.repo.ProjectRepository;
import com.example.portfolio.projects.repo.ProjectUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
	@Autowired
	private ProjectUserRepository projectUserRepository;
	@Autowired
	private ProjectRepository projectRepository;
	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}
	
	public List<ProjectUser> getAllProjectUserByUser(User user) {
		return projectRepository.getAllProjectUsersByUser(user);
	}
	
	public Optional<Project> getProjectById(Long id) {
		return projectRepository.findById(id);
	}
	
	public void saveProject(Project project, ProjectUser projectUser) {
		projectRepository.save(project);
		projectUserRepository.save(projectUser);
	}
	
	public void deleteProject(Long id) {
		projectRepository.deleteById(id);
	}
	
	public Project findById(Long id) {
		Optional<Project> projectOpt = projectRepository.findById(id);
		return projectOpt.orElseThrow(() -> new RuntimeException("Project not found with id " + id));
	}
}

