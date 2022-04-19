package com.example.meetontest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private Long id;
    private String about;
    private Long meeting_id;
    private String meetingName;
    private Long user_id;
    private String username;
    private String role;
    private String status;
}