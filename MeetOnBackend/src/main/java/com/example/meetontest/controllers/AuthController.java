package com.example.meetontest.controllers;

import com.example.meetontest.dto.GoogleLoginRequest;
import com.example.meetontest.dto.LoginRequest;
import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.dto.SignupRequest;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.services.AuthService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authService.authenticateUser(loginRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws ValidatorException {
        try {
            authService.registerUser(signUpRequest);
            return ResponseEntity.ok("Registered!");
        } catch (ValidatorException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateUserViaGoogle(@RequestBody GoogleLoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authService.authenticateUserViaGoogle(loginRequest));
        }
        catch (Exception e) {
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

    @RequestMapping(value="/confirmAccount", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam String token){
        authService.confirmUser(token);
        return ResponseEntity.ok("Registration completed!");
    }
}