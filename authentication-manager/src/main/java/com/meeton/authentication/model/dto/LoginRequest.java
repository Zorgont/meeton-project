package com.meeton.authentication.model.dto;

import lombok.*;

@Data
public class LoginRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
}