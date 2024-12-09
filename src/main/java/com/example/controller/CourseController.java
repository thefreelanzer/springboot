package com.example.controller;

import com.example.dto.CourseDto;
import com.example.service.CourseService;
import com.example.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/add")
    public ResponseEntity<CourseDto> addCourse(@RequestBody CourseDto courseDto) {
        courseDto = courseService.createCourse(courseDto);
        return new ResponseEntity<>(courseDto, HttpStatus.CREATED);
    }
}
