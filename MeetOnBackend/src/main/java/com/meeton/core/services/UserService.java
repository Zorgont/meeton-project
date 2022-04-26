package com.meeton.core.services;

import com.meeton.core.dto.UserSettingDTO;
import com.meeton.core.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    Iterable<User> getUsers();
    User createUser(User user);
    User getUserById(Long id);
    User updateUser(Long id, User newUser);
    void deleteUser(Long id);
    User getUserByName(String name);
    User getUserByEmail(String email);
    UserSettingDTO updateUserSettings(Long id, UserSettingDTO userSettingDTO);
}