package com.example.service;

import com.example.dto.OrderDto;
import com.example.dto.StudentDto;
import com.example.entity.Course;
import com.example.entity.Order;
import com.example.entity.Student;
import com.example.repository.CourseRepository;
import com.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<StudentDto> getAllStudents() {
        // Fetch all students from the repository
        List<Student> students = studentRepository.findAll();

        return students.stream().map(student -> {
            StudentDto studentDto = new StudentDto();
            studentDto.setStudentId(student.getStudentId());
            studentDto.setName(student.getName());
            studentDto.setCourses(student.getCourses());

            return studentDto;
        }).collect(Collectors.toList());

    }

    public StudentDto updateStudent(Long studentId, StudentDto studentDto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " not found"));

        student.setName(studentDto.getName());

        List<Course> courses = courseRepository.findByCourseIdIn(studentDto.getCourseIds());

        if (courses.isEmpty()) {
            throw new IllegalArgumentException("No valid courses found for the provided course IDs");
        }

        student.setCourses(new HashSet<>(courses));

        student = studentRepository.save(student);

        StudentDto updatedStudentDto = new StudentDto();
        updatedStudentDto.setStudentId(student.getStudentId());
        updatedStudentDto.setName(student.getName());
        updatedStudentDto.setCourses(student.getCourses());

        return updatedStudentDto;
    }

    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " not found"));

        student.getCourses().clear();
        studentRepository.delete(student);
    }

}
