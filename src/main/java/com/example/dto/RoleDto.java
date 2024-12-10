package com.example.dto;

import com.example.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private long id;
    private String name;
    private Set<User> users = new HashSet<>();
}
