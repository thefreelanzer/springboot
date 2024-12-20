package com.example.repository;

import com.example.entity.Course;
import com.example.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByIdIn(List<Long> ids);

    /*@Query("SELECT r FROM Role r")
    List<Role> findAllRoles();

    @Query(value = "SELECT * FROM roles", nativeQuery = true)
    List<Role> findAllRolesNative();*/

    Page<Role> findByNameContaining(String name, Pageable pageable);
}
