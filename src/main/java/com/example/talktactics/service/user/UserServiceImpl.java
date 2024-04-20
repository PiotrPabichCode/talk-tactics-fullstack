package com.example.talktactics.service.user;

import com.example.talktactics.dto.user.UpdateUserDto;
import com.example.talktactics.dto.user.UserProfilePreviewDto;
import com.example.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.example.talktactics.exception.UserRuntimeException;
import com.example.talktactics.entity.*;
import com.example.talktactics.repository.UserRepository;
import com.example.talktactics.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static com.example.talktactics.util.EmailValidator.isValidEmail;
import static com.example.talktactics.util.Utils.getJsonPropertyFieldMap;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

//  PUBLIC
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public List<User> getUsers() {
        validateAdmin();
        return userRepository.findAll();
    }
    @Override
    public User getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserRuntimeException(Constants.USER_NOT_FOUND_EXCEPTION));
        validateCredentials(user);
        return user;
    }
    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserRuntimeException(Constants.USER_NOT_FOUND_EXCEPTION));
        validateCredentials(user);
        return user;
    }
    @Override
    public void deleteUser(long id) {
        validateAdmin();
        if(!userRepository.existsById(id)) {
            throw new UserRuntimeException(Constants.USER_NOT_FOUND_EXCEPTION);
        }

        User user = getUserById(id);
        if(user.getRole().toString().equals(Constants.ADMIN)) {
            throw new UserRuntimeException("Cannot delete admin user");
        }
        userRepository.deleteById(id);
    }
    @Override
    public User updateUser(long id, Map<String, Object> fields) {
        User user = getUserById(id);
        validateCredentials(user);
        validateFields(fields);

        Map<String, String> fieldMap = getJsonPropertyFieldMap(UpdateUserDto.class);

        fields.forEach((key, value) -> {
            String fieldName = fieldMap.get(key);
            if (fieldName == null) {
                throw new UserRuntimeException("Illegal fields given: " + key);
            }
            Field field = ReflectionUtils.findField(User.class, fieldName);
            if (field == null) {
                throw new UserRuntimeException("Field not found: " + fieldName);
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, value);
        });

        return userRepository.save(user);
    }
    @Override
    public void validateAdmin() {
        if(!isAdmin()) {
            throw new UserRuntimeException(Constants.NOT_ENOUGH_AUTHORITIES_EXCEPTION);
        }
    }
    @Override
    public void validateCredentials(User user) {
        if(!isAdmin() && !isCurrentUser(user)) {
            throw new UserRuntimeException(Constants.NOT_ENOUGH_AUTHORITIES_EXCEPTION);
        }
    }

    @Override
    public void validateFields(Map<String, Object> fields) {
        if(fields.containsKey("email")) {
            String email = (String) fields.get("email");
            if(!isValidEmail(email)) {
                throw new UserRuntimeException(Constants.EMAIL_FORBIDDEN_VALUES_EXCEPTION);
            }
        }
    }
    @Override
    public User updatePassword(UpdatePasswordReqDto req) {
        User user = getUserById(req.getId());

        validateCredentials(user);
        validatePassword(user, req);

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<UserProfilePreviewDto> getUserProfiles() {
        List<User> users = userRepository.findAll();
        List<UserProfilePreviewDto> userProfiles = users.stream()
                .sorted(Comparator.comparingInt(User::getTotalPoints).reversed())
                .map(User::toUserProfilePreviewDto)
                .toList();
        return userProfiles;
    }

    //  PRIVATE
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Constants.ADMIN));
    }


    private boolean isCurrentUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName().equals(user.getUsername());
    }

    private void validatePassword(User user, UpdatePasswordReqDto req) {
        if(req.getOldPassword().isBlank() || req.getNewPassword().isBlank() || req.getRepeatNewPassword().isBlank()) {
            throw new UserRuntimeException(Constants.ALL_FIELDS_REQUIRED);
        }
        if(passwordEncoder.matches(user.getPassword(), req.getOldPassword())) {
            throw new UserRuntimeException(Constants.INVALID_PASSWORD_EXCEPTION);
        }
        if(!req.getNewPassword().equals(req.getRepeatNewPassword())) {
            throw new UserRuntimeException(Constants.THE_SAME_PASSWORD_EXCEPTION);
        }
        if(passwordEncoder.matches(user.getPassword(), req.getNewPassword())) {
            throw new UserRuntimeException(Constants.DUPLICATED_PASSWORD_EXCEPTION);
        }
    }

}
