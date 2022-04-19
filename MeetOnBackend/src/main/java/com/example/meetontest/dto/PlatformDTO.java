package com.example.meetontest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlatformDTO {
    private Long id;
    private String name;
    private String info;
    //    private ??? logo;
    private String type;
}
