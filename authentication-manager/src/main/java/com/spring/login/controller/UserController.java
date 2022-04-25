package com.spring.login.controller;

import com.spring.login.exception.BadRequestException;
import com.spring.login.exception.ResourceNotFoundException;
import com.spring.login.model.LoginRequest;
import com.spring.login.model.SignUpRequest;
import com.spring.login.model.User;
import com.spring.login.security.CurrentUser;
import com.spring.login.security.UserPrincipal;
import com.spring.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(userService.login(loginRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        try {
            userService.createUser(signUpRequest);
            return ResponseEntity.ok("Registered!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
