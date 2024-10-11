package com.example.portfolio.access.repo;

import com.example.portfolio.access.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.portfolio.access.model.User; // User 엔티티 임포트

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	// 사용자 이름으로 User를 조회하는 메서드
	User findByUsername(String username);
	// username으로 존재 여부 확인하는 메소드
	boolean existsByUsername(String username);
	
	@Query("SELECT ur.role FROM User u JOIN UserRole ur ON u.id = ur.id WHERE u.username = :username")
	Role findRoleByUsername(@Param("username") String username);
}