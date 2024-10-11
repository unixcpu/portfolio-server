package com.example.portfolio.access.model;

import com.example.portfolio.projects.model.ProjectUser;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private String password;
	private String email;
	private String profilePicUrl; // 프로필 사진 URL 추가
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRole> userRoles = new ArrayList<>(); // 빈 리스트로 초기화
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<ProjectUser> projectUsers; // 사용자가 소속된 ProjectUser 리스트
	
	// Getter 및 Setter
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<UserRole> getUserRoles() {
		return userRoles != null ? userRoles : new ArrayList<>();
	}
	
	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
	public String getProfilePicUrl() {
		return profilePicUrl;
	}
	
	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}
}
