package com.example.talktactics.service.user_course;

import com.example.talktactics.dto.user_course.UserCoursePreviewDto;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseGetReqDto;
import com.example.talktactics.dto.user_course.res.UserCourseResponseDto;
import com.example.talktactics.entity.UserCourse;

import java.util.List;

public interface UserCourseService {
    List<UserCourse> getAllUserCourses();
    List<UserCourseResponseDto> getAllByUserId(long userID);
    List<UserCoursePreviewDto> getUserCoursesPreviewListByUserId(long userId);
    UserCourse getById(long id);
    void addUserCourse(UserCourseAddReqDto req);
    void deleteUserCourse(UserCourseDeleteReqDto req);
    UserCourse getByUserIdAndCourseId(UserCourseGetReqDto req);
}
