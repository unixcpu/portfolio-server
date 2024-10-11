package com.example.portfolio.projects.repo;

import com.example.portfolio.access.model.User;
import com.example.portfolio.projects.model.Project;
import com.example.portfolio.projects.model.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	@Query("SELECT pu.project FROM ProjectUser pu WHERE pu = :projectUser")
	List<Project> getAllProjectsByProjectUser(@Param("projectUser") ProjectUser projectUser);
	
	@Query("SELECT pu FROM ProjectUser pu WHERE pu.user = :user")
	List<ProjectUser> getAllProjectUsersByUser(@Param("user") User user);
}