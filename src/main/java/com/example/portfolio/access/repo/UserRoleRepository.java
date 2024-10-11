package com.example.portfolio.access.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.portfolio.access.model.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	
	@Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId")
	List<UserRole> findByUserId(@Param("userId") Long userId);
	
	@Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :roleId AND ur.isRemoved = false ORDER BY ur.id ASC")
	Optional<UserRole> findFirstByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
