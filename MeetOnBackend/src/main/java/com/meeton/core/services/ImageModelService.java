package com.meeton.core.services;

import com.meeton.core.entities.ImageModel;

public interface ImageModelService {
    ImageModel getUserAvatar(String username);
    ImageModel updateOrCreateUserAvatar(Long userId, ImageModel avatar);
}
