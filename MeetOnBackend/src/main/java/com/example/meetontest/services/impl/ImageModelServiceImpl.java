package com.example.meetontest.services.impl;

import com.example.meetontest.entities.ImageModel;
import com.example.meetontest.entities.User;
import com.example.meetontest.repositories.ImageModelRepository;
import com.example.meetontest.services.ImageModelService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageModelServiceImpl implements ImageModelService {
    private final UserService userService;
    private final ImageModelRepository imageModelRepository;
    @Override
    public ImageModel getUserAvatar(Long userId) {
        return userService.getUserById(userId).getAvatar();
    }

    @Override
    public ImageModel updateOrCreateUserAvatar(Long userId, ImageModel avatar) {
        User user = userService.getUserById(userId);
        imageModelRepository.save(avatar);
        user.setAvatar(avatar);
        userService.createUser(user);
        return avatar;
    }
}
