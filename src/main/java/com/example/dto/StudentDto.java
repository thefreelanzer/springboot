package com.example.dto;

import com.example.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Long studentId;

    private String name;

    private List<Long> courseIds;

    private Set<Course> courses = new HashSet<>();
}
