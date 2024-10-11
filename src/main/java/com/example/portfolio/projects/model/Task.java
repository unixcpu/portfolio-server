package com.example.portfolio.projects.model;

import jakarta.persistence.*;

@Entity
@Table(name = "task")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	private String description;
	private String status;
	
	@ManyToOne // Many tasks can belong to one project
	@JoinColumn(name = "project_id") // FK 컬럼 이름
	private Project project; // Project 참조 추가
	
	private Integer priority;  // 우선순위 필드 추가
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getPriority() {
		return priority;
	}
	
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	
	// Getter 및 Setter 메서드들
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setProjectId(Long projectId) {
	}
}
