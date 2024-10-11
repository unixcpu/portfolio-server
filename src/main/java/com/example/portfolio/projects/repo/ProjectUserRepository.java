package com.example.portfolio.projects.repo;

import com.example.portfolio.projects.model.Project;
import com.example.portfolio.projects.model.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
	// 필요한 메서드를 여기에 추가할 수 있습니다.
}
