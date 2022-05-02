package com.meeton.authentication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
public class SignUpRequest {
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;
}
