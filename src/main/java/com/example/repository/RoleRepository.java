package com.example.repository;

import com.example.entity.Course;
import com.example.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByIdIn(List<Long> ids);
}
