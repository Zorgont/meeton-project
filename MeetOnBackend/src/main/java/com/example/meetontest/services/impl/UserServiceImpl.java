package com.example.meetontest.services.impl;

import com.example.meetontest.dto.UserSettingDTO;
import com.example.meetontest.entities.User;
import com.example.meetontest.exceptions.ResourceNotFoundException;
import com.example.meetontest.rating.service.UserRatingProvider;
import com.example.meetontest.repositories.UserRepository;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Autowired
    private UserRatingProvider userRatingProvider;

    @Override
    public Iterable<User> getUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setKarma(userRatingProvider.getUserRating(user)));
        return users;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
        user.setKarma(userRatingProvider.getUserRating(user));
        return user;
    }

    @Override
    public User updateUser(Long id, User newUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
        user.setFirstName(newUser.getFirstName());
        user.setAbout(newUser.getAbout());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!")));
    }

    @Override
    public User getUserByName(String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
        user.setKarma(userRatingProvider.getUserRating(user));
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
        user.setKarma(userRatingProvider.getUserRating(user));
        return user;
    }

    @Override
    public UserSettingDTO updateUserSettings(Long id, UserSettingDTO userSettingDTO) {
        User entity = userRepository.findById(id).get();
        entity.setFirstName(userSettingDTO.getFirstName());
        entity.setSecondName(userSettingDTO.getSecondName());
        entity.setAbout(userSettingDTO.getAbout());
        userRepository.save(entity);
        return userSettingDTO;
    }

}