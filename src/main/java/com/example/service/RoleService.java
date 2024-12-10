package com.example.service;

import com.example.dto.RoleDto;
import com.example.entity.Role;
import com.example.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public RoleDto createRole(RoleDto roleDto) {

        Role role = new Role();
        role.setName(roleDto.getName());

        role = roleRepository.save(role);
        roleDto.setId(role.getId());
        return roleDto;
    }

    public List<RoleDto> getAllRoles() {
        // List<Role> roles = roleRepository.findAllRoles(); // using @Query
        List<Role> roles = roleRepository.findAllRolesNative(); // Using native query

        return roles.stream().map(role -> {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(role.getId());
            roleDto.setName(role.getName());
            roleDto.setUsers(role.getUsers());
            return roleDto;
        }).collect(Collectors.toList());

    }
}
