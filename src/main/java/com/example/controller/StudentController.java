package com.example.controller;

import com.example.dto.OrderDto;
import com.example.dto.StudentDto;
import com.example.service.OrderService;
import com.example.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllOrders() {
        List<StudentDto> student = studentService.getAllStudents();
        return ResponseEntity.ok(student);
    }

    @PostMapping("/add")
    public ResponseEntity<StudentDto> addOrder(@Valid @RequestBody StudentDto studentDto) {
        studentDto = studentService.createStudent(studentDto);
        return new ResponseEntity<>(studentDto, HttpStatus.CREATED);
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto) {
        StudentDto updateStudent = studentService.updateStudent(id, studentDto);
        return ResponseEntity.ok(updateStudent);
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student with ID " + id + " has been deleted.");
    }
}
