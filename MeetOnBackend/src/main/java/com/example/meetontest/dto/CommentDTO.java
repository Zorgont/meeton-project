package com.example.meetontest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private Long meeting_id;
    private String meetingName;
    private Long user_id;
    private String username;
    private String content;
    private String date;
}
