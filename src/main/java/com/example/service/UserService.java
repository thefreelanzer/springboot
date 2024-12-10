package com.example.service;

import com.example.dto.RoleDto;
import com.example.dto.StudentDto;
import com.example.dto.UserDto;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto createUser(UserDto userDto) {

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());


        List<Role> roles = roleRepository.findByIdIn(userDto.getRoleIds());
        System.out.println(roles);

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("No valid roles found!");
        }

        user.setRoles(new HashSet<>(roles));
        user = userRepository.save(user);
        userDto.setId(user.getId());
        userDto.setRoles(user.getRoles());

        return userDto;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            UserDto userDto = new UserDto();
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setRoles(user.getRoles());
            return userDto;
        }).collect(Collectors.toList());

    }

    /*public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found!"));

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(userDto.getPassword());

        List<Role> roles = roleRepository.findByIdIn(userDto.getRoleIds());
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("No valid roles found!");
        }

        existingUser.setRoles(new HashSet<>(roles));

        existingUser = userRepository.save(existingUser);
        userDto.setId(existingUser.getId());
        userDto.setRoles(existingUser.getRoles());

        return userDto;
    }*/

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        System.out.println("updateUser started........................................");
        int rowsUpdated = userRepository.updateUserInfo(id, userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getPassword());
        System.out.println(rowsUpdated);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("User with id " + id + " not found!");
        }
        System.out.println("\n1-------------");

        List<Role> roles = roleRepository.findByIdIn(userDto.getRoleIds());
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("No valid roles found!");
        }
        System.out.println("\n2-------------");

        int rolesUpdated = userRepository.updateUserRoles(id, new HashSet<>(roles));
        if (rolesUpdated == 0) {
            throw new IllegalArgumentException("Failed to update roles for user with id " + id);
        }

        System.out.println("\n3-------------");

        // Return the updated user DTO
        userDto.setId(id);
        userDto.setRoles(new HashSet<>(roles));

        return userDto;
    }

    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found!"));

        userRepository.delete(existingUser);
    }
}
