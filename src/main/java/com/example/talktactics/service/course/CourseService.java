package com.example.talktactics.service.course;

import com.example.talktactics.dto.course.CourseDto;
import com.example.talktactics.exception.CourseNotFoundException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
    public List<CourseDto> getCourses() {
        List<Course> courses = courseRepository.findAll(Sort.by("id"));
        return courses.stream()
                .map(Course::toDTO)
                .collect(Collectors.toList());
    }
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
    }
    public List<Course> filterCoursesByLevelName(String levelName) {
        return courseRepository.findByLevelName(levelName);
    }
    public Course updateCourse(Long id, Course newCourse) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setDescription(newCourse.getDescription());
                    course.setLevel(newCourse.getLevel());
                    course.setTitle(newCourse.getTitle());
                    return courseRepository.save(course);
                }).orElseThrow(() -> new CourseNotFoundException(id));
    }
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException(id);
        }
        courseRepository.deleteById(id);
    }
}