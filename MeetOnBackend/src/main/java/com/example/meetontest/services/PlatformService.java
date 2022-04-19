package com.example.meetontest.services;

import com.example.meetontest.entities.Platform;
import com.example.meetontest.exceptions.ValidatorException;

import java.util.List;
import java.util.Optional;

public interface PlatformService {
    List<Platform> getAll();

    Optional<Platform> getById(Long id);

    Optional<Platform> getByName(String name);

    Platform create(Platform platform) throws ValidatorException;

    Platform update(Long id, Platform platform) throws ValidatorException;

    boolean delete(Long id);

    boolean delete(Platform platform);
}
