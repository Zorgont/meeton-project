package com.spring.login.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

@Data
@Document
public class User {

    @Id
    private String id;

    private String username;

    private String firstName;
    private String secondName;
    private String name;

    private String email;

    private String imageUrl;

    private Boolean emailVerified = false;

    @JsonIgnore
    private String password = null;

    private AuthProvider provider;

    private String providerId;

    private boolean valid;
}
