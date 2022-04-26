package com.meeton.authentication.controller;

import com.meeton.authentication.exception.BadRequestException;
import com.meeton.authentication.exception.ResourceNotFoundException;
import com.meeton.authentication.model.LoginRequest;
import com.meeton.authentication.model.SignUpRequest;
import com.meeton.authentication.model.User;
import com.meeton.authentication.security.CurrentUser;
import com.meeton.authentication.security.UserPrincipal;
import com.meeton.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "authentication-manager/v1/auth")
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
