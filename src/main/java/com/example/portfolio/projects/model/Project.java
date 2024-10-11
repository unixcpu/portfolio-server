package com.example.portfolio.projects.model;

import com.example.portfolio.access.model.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String description;
	
	@ManyToOne // User와의 다대일 관계 설정
	@JoinColumn(name = "user_id", nullable = false) // 외래 키 컬럼
	private User owner; // owner를 String에서 User로 변경
	
	private Date startDate; // 시작일
	private Date dueDate; // 마감일
	private String priority; // 우선순위
	private String tags; // 태그
	private String status; // 상태
	private String type; // 프로젝트 유형
	private String color; // 색상
	private String updateFile; // 첨부파일
	private String updateFileUrl; // 첨부파일
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private List<Task> tasks = new ArrayList<>();
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private List<ProjectUser> projectUsers; // 프로젝트와 연결된 ProjectUser 리스트
	
	public List<Task> getTasks() {
		return tasks;
	}
	
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	// Getter와 Setter
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner; // User 객체로 설정
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public String getPriority() {
		return priority;
	}
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public String getTags() {
		return tags;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getUpdateFileUrl() {
		return updateFileUrl;
	}
	
	public void setUpdateFileUrl(String updateFileUrl) {
		this.updateFileUrl = updateFileUrl;
	}
	public String getUpdateFile() {
		return updateFile;
	}
	
	public void setUpdateFile(String updateFile) {
		this.updateFile = updateFile;
	}
	
	public void setProjectUsers(List<ProjectUser> projectUsers) {
		this.projectUsers = projectUsers;
	}
	
	public List<ProjectUser> getProjectUsers() {
		return projectUsers;
	}
	
}
