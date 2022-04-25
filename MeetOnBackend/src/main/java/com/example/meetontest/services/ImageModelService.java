package com.example.meetontest.services;

import com.example.meetontest.entities.ImageModel;

public interface ImageModelService {
    ImageModel getUserAvatar(String username);
    ImageModel updateOrCreateUserAvatar(Long userId, ImageModel avatar);
}
