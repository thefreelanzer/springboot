package com.example.service;

import com.example.dto.StudentDto;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.repository.CourseRepository;
import com.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentService(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public StudentDto createStudent(StudentDto studentDto) {
        Student student = new Student();
        student.setName(studentDto.getName());

        List<Course> courses = courseRepository.findByCourseIdIn(studentDto.getCourseIds());

        if (courses.isEmpty()) {
            throw new IllegalArgumentException("No valid courses found for the provided course codes");
        }

        // Set the courses to the student
        student.setCourses(new HashSet<>(courses));

        student = studentRepository.save(student);
        studentDto.setStudentId(student.getStudentId());
        studentDto.setCourses(student.getCourses());
        return studentDto;
    }
}
