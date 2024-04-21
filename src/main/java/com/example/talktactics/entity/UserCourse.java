package com.example.talktactics.entity;

import com.example.talktactics.common.CommonEntity;
import com.example.talktactics.dto.user_course.UserCourseDetailsDto;
import com.example.talktactics.listeners.UserCourseEntityListeners;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "user_courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(UserCourseEntityListeners.class)
public class UserCourse extends CommonEntity {
    private double progress = 0.0;
    @JsonProperty("is_completed")
    private boolean completed = false;
    private int points = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


    @JsonIgnore
    @OneToMany(mappedBy = "userCourse",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<UserCourseItem> userCourseItems;

    public UserCourseDetailsDto toUserCourseDetailsDto() {
        Course course = this.getCourse();

        return UserCourseDetailsDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .level(course.getLevel())
                .quantity(course.getQuantity())
                .completed(this.isCompleted())
                .points(this.getPoints())
                .progress(this.getProgress())
                .build();
    }
}
