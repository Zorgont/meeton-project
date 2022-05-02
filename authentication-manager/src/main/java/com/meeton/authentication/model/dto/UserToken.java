package com.meeton.authentication.model.dto;

import com.meeton.authentication.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserToken {
    private User user;
    private String token;
}
