package com.example.controller;

import com.example.dto.RoleDto;
import com.example.dto.StudentDto;
import com.example.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
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

    /*@GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = (Pageable) PageRequest.of(page, size);

        Page<RoleDto> rolePage;
        if (title != null && !title.isEmpty()) {
            rolePage = roleService.getRolesByTitle(title, pageable);
        } else {
            rolePage = roleService.getAllRoles(pageable);
        }
        return ResponseEntity.ok(rolePage.getContent());
    }*/

    @PostMapping("/add")
    public ResponseEntity<RoleDto> addRole(@Valid @RequestBody RoleDto roleDto) {
        roleDto = roleService.createRole(roleDto);
        return new ResponseEntity<>(roleDto, HttpStatus.CREATED);
    }
}
