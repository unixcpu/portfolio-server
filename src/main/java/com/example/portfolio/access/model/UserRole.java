package com.example.portfolio.access.model;

import jakarta.persistence.*;

@Entity
public class UserRole {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	private String roleName; // 추가된 필드
	
	private Boolean isRemoved = false; // 새로 추가된 필드
	
	public Boolean getRemoved() {
		return isRemoved;
	}
	
	public void setRemoved(Boolean removed) {
		isRemoved = removed;
	}
	
	// Getter 및 Setter
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
		if (role != null) {
			this.roleName = role.getName(); // 역할이 설정되면 역할 이름을 동기화
		}
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
