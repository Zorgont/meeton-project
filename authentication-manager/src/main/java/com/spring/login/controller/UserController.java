package com.spring.login.controller;

import com.spring.login.exception.BadRequestException;
import com.spring.login.exception.ResourceNotFoundException;
import com.spring.login.model.User;
import com.spring.login.security.CurrentUser;
import com.spring.login.security.UserPrincipal;
import com.spring.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getUserByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userPrincipal.getEmail()));
    }

    @GetMapping("/updateUsername")
    public User updateUsername(@CurrentUser UserPrincipal userPrincipal, @RequestParam String username) {
        User user = userService.getUserByEmail(userPrincipal.getEmail()).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "email", userPrincipal.getEmail());
        });

        if (!user.isValid()) {
            user.setUsername(username);
            userService.save(user);
        } else {
            throw new BadRequestException("User is already valid!");
        }
        return user;
    }
}
