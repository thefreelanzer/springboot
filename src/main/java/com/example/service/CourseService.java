package com.example.service;

import com.example.dto.CourseDto;
import com.example.entity.Course;
import com.example.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public CourseDto createCourse(CourseDto courseDto) {

        Course course = new Course();
        course.setName(courseDto.getName());
        course.setCourseCode(courseDto.getCourseCode());

        course = courseRepository.save(course);
        courseDto.setCourseId(course.getCourseId());
        return courseDto;
    }
}
