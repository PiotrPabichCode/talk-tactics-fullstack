package com.example.talktactics.repositories;

import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Course;
import com.example.talktactics.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE LOWER(t.course.name) LIKE LOWER(CONCAT('%', :courseName, '%'))")
    ArrayList<Task> findByCourseNameContaining(@Param("courseName") String courseName);

    @Query("SELECT t FROM Task t JOIN t.course c WHERE c.id = :course_id")
    List<Task> getTasksByCourseId(@Param("course_id") int course_id);

    List<Task> findByCourse(Course course);
}