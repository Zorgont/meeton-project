package com.spring.login.security.oauth2.user;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("login");
    }

    @Override
    public String getFirstName() {
        try {
            String name = (String) attributes.get("name");
            return name.split("\\s+")[0];
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getSecondName() {
        try {
            String name = (String) attributes.get("name");
            return name.split("\\s+")[1];
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}
