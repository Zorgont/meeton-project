package com.meeton.core.services.impl;

import com.meeton.core.entities.ImageModel;
import com.meeton.core.entities.User;
import com.meeton.core.repositories.ImageModelRepository;
import com.meeton.core.services.ImageModelService;
import com.meeton.core.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageModelServiceImpl implements ImageModelService {
    private final UserService userService;
    private final ImageModelRepository imageModelRepository;
    @Override
    public ImageModel getUserAvatar(String username) {
        return userService.getUserByName(username).getAvatar();
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
