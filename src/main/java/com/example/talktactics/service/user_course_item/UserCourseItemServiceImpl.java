package com.example.talktactics.service.user_course_item;

import com.example.talktactics.dto.user_course_item.UserCourseItemPreviewDto;
import com.example.talktactics.dto.user_course_item.req.GetUserCourseItemsPreviewDtoReq;
import com.example.talktactics.dto.user_course_item.res.GetUserCourseItemPreviewDtoResponse;
import com.example.talktactics.entity.User;
import com.example.talktactics.exception.UserCourseItemRuntimeException;
import com.example.talktactics.entity.UserCourse;
import com.example.talktactics.entity.UserCourseItem;
import com.example.talktactics.exception.UserCourseRuntimeException;
import com.example.talktactics.repository.UserCourseItemRepository;
import com.example.talktactics.service.course.CourseServiceImpl;
import com.example.talktactics.service.user.UserServiceImpl;
import com.example.talktactics.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserCourseItemServiceImpl implements UserCourseItemService {
    private final UserCourseItemRepository userCourseItemRepository;
    private final CourseServiceImpl courseServiceImpl;
    private final UserServiceImpl userServiceImpl;

//  PUBLIC
    public UserCourseItem getById(Long id) {
        UserCourseItem userCourseItem = userCourseItemRepository.findById(id).orElseThrow(() -> new UserCourseItemRuntimeException(Constants.USER_COURSE_ITEM_NOT_FOUND_EXCEPTION));
        User user = userCourseItem.getUserCourse().getUser();
        userServiceImpl.validateCredentials(user);
        return userCourseItem;
    }

    public void updateIsLearned(Long id) {
        UserCourseItem userCourseItem = getById(id);
        UserCourse userCourse = userCourseItem.getUserCourse();
        double value = 100.0 / userCourse.getUserCourseItems().size();
        userCourse.setProgress(userCourse.getProgress() + (!userCourseItem.isLearned() ? value : -value));

        double tolerance = 0.0001;
        userCourse.setCompleted(Math.abs(userCourse.getProgress() - 100.0) < tolerance);
        userCourseItem.setLearned(!userCourseItem.isLearned());
        userCourseItemRepository.save(userCourseItem);
    }
    public GetUserCourseItemPreviewDtoResponse getUserCourseItemPreviewDtoResponse(GetUserCourseItemsPreviewDtoReq req) {
        List<UserCourseItemPreviewDto> items = getAllByUserIdAndCourseId(req.getUserId(), req.getCourseId());
        String courseName = courseServiceImpl.getById(req.getCourseId()).getTitle();
        return new GetUserCourseItemPreviewDtoResponse(courseName, items);
    }

//  PRIVATE
    private List<UserCourseItemPreviewDto> getAllByUserIdAndCourseId(Long userId, Long courseId) {
        User user = userServiceImpl.getUserById(userId);
        userServiceImpl.validateCredentials(user);
        List<UserCourseItemPreviewDto> items = userCourseItemRepository.findAllByUserCourseCourseIdAndUserCourseUserId(courseId, userId).stream().map(UserCourseItem::toUserCourseItemPreviewDto).collect(Collectors.toList());
        if(items.isEmpty()) {
            throw new UserCourseRuntimeException(Constants.USER_COURSE_ITEM_NOT_FOUND_EXCEPTION);
        }
        items.sort(Comparator.comparingInt(UserCourseItemPreviewDto::getId));
        return items;
    }
}
