package com.example.talktactics.entity;

import com.example.talktactics.common.CommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "meanings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Meaning extends CommonEntity {
    @Column(length = 800)
    private String definition;
    @Column(length = 800)
    private String example;

    @JsonIgnoreProperties("meanings")
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_item_id")
    private CourseItem courseItem;
}
