package com.spring.login.model;

import lombok.*;

@Data
public class LoginRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
}