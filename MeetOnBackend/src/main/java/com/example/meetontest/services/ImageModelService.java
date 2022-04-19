package com.example.meetontest.services;

import com.example.meetontest.entities.ImageModel;

public interface ImageModelService {
    ImageModel getUserAvatar(Long userId);
    ImageModel updateOrCreateUserAvatar(Long userId, ImageModel avatar);
}
