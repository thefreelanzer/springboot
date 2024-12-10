package com.example.controller;

import com.example.dto.RoleDto;
import com.example.dto.StudentDto;
import com.example.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /*@GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> role = roleService.getAllRoles();
        return ResponseEntity.ok(role);
    }*/

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRoles(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<RoleDto> rolePage;

            if (name != null && !name.isEmpty()) {
                rolePage = roleService.getRolesByName(name, pageable);
            } else {
                rolePage = roleService.getAllRoles(pageable);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("data", rolePage.getContent());
            response.put("currentPage", rolePage.getNumber());
            response.put("totalItems", rolePage.getTotalElements());
            response.put("totalPages", rolePage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<RoleDto> addRole(@Valid @RequestBody RoleDto roleDto) {
        roleDto = roleService.createRole(roleDto);
        return new ResponseEntity<>(roleDto, HttpStatus.CREATED);
    }
}
