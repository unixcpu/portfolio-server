package com.example.portfolio.access.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserRole> userRoles = new HashSet<>();
	
	// Getter 및 Setter
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
	
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}
	
	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
}
