package com.meeton.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

@Data
@Builder
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

    private Boolean emailVerified;

    @JsonIgnore
    private String password;

    private AuthProvider provider;

    private String providerId;

    private boolean valid;
}
