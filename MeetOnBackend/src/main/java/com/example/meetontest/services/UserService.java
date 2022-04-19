package com.example.meetontest.services;

import com.example.meetontest.dto.UserSettingDTO;
import com.example.meetontest.entities.User;
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