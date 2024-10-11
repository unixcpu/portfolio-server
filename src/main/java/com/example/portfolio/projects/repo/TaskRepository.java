package com.example.portfolio.projects.repo;

import com.example.portfolio.projects.model.Project;
import com.example.portfolio.projects.model.Task;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // 프로젝트 ID로 태스크 목록을 가져오는 메서드
    List<Task> findByProject(Project project); // 프로젝트로 태스크 찾기
    
    List<Task> findByProjectOrderByPriorityAsc(Project project);
    
    @Modifying
    @Query("UPDATE Task t SET t.priority = :priority WHERE t.id = :taskId")
    int updateTaskPriorityById(@Param("taskId") Long taskId, @Param("priority") Integer priority);
}
