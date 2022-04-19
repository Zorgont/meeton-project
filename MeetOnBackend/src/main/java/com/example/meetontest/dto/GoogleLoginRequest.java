package com.example.meetontest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleLoginRequest {
    private String email;
    private String username;
    private String firstName;
    private String secondName;
    private String imageUrl;
    private String accessToken;
}
