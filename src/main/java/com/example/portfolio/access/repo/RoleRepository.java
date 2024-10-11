package com.example.portfolio.access.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.portfolio.access.model.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findById(Long id);
	Optional<Role> findByName(String name);
}
