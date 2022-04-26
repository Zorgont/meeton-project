package com.meeton.core.services.impl;

import com.meeton.core.entities.Platform;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.repositories.PlatformRepository;
import com.meeton.core.services.PlatformService;
import com.meeton.core.exceptions.ValidatorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlatformServiceImpl implements PlatformService {
    private final PlatformRepository platformRepository;

    @Override
    public List<Platform> getAll() {
        return platformRepository.findAll();
    }

    @Override
    public Optional<Platform> getById(Long id) {
        return platformRepository.findById(id);
    }

    @Override
    public Optional<Platform> getByName(String name) {
        return platformRepository.findByName(name);
    }

    @Override
    public Platform create(Platform platform) {
        if (platformRepository.existsByName(platform.getName()))
            throw new ValidatorException("Platform " + platform.getName() + " is already exists!");

        return platformRepository.save(platform);
    }

    @Override
    public Platform update(Long id, Platform newPlatform) {
        Optional<Platform> platform = platformRepository.findById(id);
        if (!platform.isPresent())
            throw new ValidatorException("Platform with id " + id + " was not found!");

        platform.get().setName(newPlatform.getName());
        platform.get().setInfo(newPlatform.getInfo());
        platform.get().setType(newPlatform.getType());

        return platform.get();
    }

    @Override
    public boolean delete(Long id) {
        platformRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean delete(Platform platform) {
        platformRepository.delete(platform);
        return true;
    }
}
