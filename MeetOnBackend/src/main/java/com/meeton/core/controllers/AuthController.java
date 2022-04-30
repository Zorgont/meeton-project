package com.meeton.core.controllers;

import com.meeton.core.dto.MessageResponse;
import com.meeton.core.dto.SignupRequest;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.services.AuthService;
import com.meeton.core.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/meeton-core/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws ValidatorException {
        try {
            authService.registerUser(signUpRequest);
            return ResponseEntity.ok("Registered!");
        } catch (ValidatorException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/existsByName/{username}")
    public boolean existsByUsername(@PathVariable String username) {
        try {
            userService.getUserByName(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/existsByEmail/{email}")
    public boolean existsByEmail(@PathVariable String email) {
        try {
            userService.getUserByEmail(email);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}