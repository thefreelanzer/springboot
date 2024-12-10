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
    @Query(value = "DELETE FROM users_roles WHERE user_id = :userId", nativeQuery = true)
    void deleteUserRoles(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_roles (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
    void addUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
