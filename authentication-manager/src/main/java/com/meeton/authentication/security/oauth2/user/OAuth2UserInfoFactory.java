package com.meeton.authentication.security.oauth2.user;

import com.meeton.authentication.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

import static com.meeton.authentication.model.entity.AuthProvider.github;
import static com.meeton.authentication.model.entity.AuthProvider.google;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if(registrationId.equalsIgnoreCase(github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
