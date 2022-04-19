package com.example.meetontest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDTO {
    private Long user_id;
    private Long id;
    private Long meeting_id;
    private Integer score;
    private Date date;
}
