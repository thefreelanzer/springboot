package com.example.controller;

import com.example.dto.RoleDto;
import com.example.dto.StudentDto;
import com.example.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> role = roleService.getAllRoles();
        return ResponseEntity.ok(role);
    }

    @PostMapping("/add")
    public ResponseEntity<RoleDto> addRole(@Valid @RequestBody RoleDto roleDto) {
        roleDto = roleService.createRole(roleDto);
        return new ResponseEntity<>(roleDto, HttpStatus.CREATED);
    }
}
