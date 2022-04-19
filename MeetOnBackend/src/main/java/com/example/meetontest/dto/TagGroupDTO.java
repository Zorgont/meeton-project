package com.example.meetontest.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagGroupDTO {
    List<String> tags;
    Long groupId;
    Long userId;
    Boolean isNotifiable;
}
