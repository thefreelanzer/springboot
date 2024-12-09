package com.example.controller;

import com.example.dto.OrderDto;
import com.example.dto.StudentDto;
import com.example.service.OrderService;
import com.example.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/add")
    public ResponseEntity<StudentDto> addOrder(@Valid @RequestBody StudentDto studentDto) {
        studentDto = studentService.createStudent(studentDto);
        return new ResponseEntity<>(studentDto, HttpStatus.CREATED);
    }
}
