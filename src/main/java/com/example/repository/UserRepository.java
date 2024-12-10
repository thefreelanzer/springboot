package com.example.repository;

import com.example.entity.Role;
import com.example.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, u.email = :email, u.password = :password WHERE u.id = :id")
    int updateUserInfo(@Param("id") Long id, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email, @Param("password") String password);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.roles = :roles WHERE u.id = :id")
    int updateUserRoles(@Param("id") Long id, @Param("roles") Set<Role> roles);
}
