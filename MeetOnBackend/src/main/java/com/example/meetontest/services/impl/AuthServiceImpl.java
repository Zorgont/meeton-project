package com.example.meetontest.services.impl;

import com.example.meetontest.dto.GoogleLoginRequest;
import com.example.meetontest.dto.JwtResponse;
import com.example.meetontest.dto.LoginRequest;
import com.example.meetontest.dto.SignupRequest;
import com.example.meetontest.entities.ConfirmationToken;
import com.example.meetontest.entities.ERole;
import com.example.meetontest.entities.Role;
import com.example.meetontest.entities.User;
import com.example.meetontest.exceptions.ResourceNotFoundException;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.repositories.RoleRepository;
import com.example.meetontest.repositories.UserRepository;
import com.example.meetontest.security.JwtUtils;
import com.example.meetontest.services.AuthService;
import com.example.meetontest.services.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final private AuthenticationManager authenticationManager;
    final private UserRepository userRepository;
    final private RoleRepository roleRepository;
    final private EmailService emailService;
    final private ConfirmationTokenService confirmationTokenService;
    final private PasswordEncoder encoder;
    final private JwtUtils jwtUtils;

    @Value("${meeton.app.jwtValidation.google}")
    private String googleUrl;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwt = jwtUtils.generateJwtToken(userDetails);
        return new JwtResponse(
                userDetails.getId(),
                jwt, userDetails.getUsername(),
                userDetails.getEmail(), roles);
    }

    @Override
    public JwtResponse authenticateUserViaGoogle(GoogleLoginRequest loginRequest) {
        // проверить токен
        if (!confirmGoogleToken(loginRequest.getAccessToken(), loginRequest.getEmail()))
            throw new ValidatorException("Error: Wrong access token!");

        User user;
        if (!userRepository.existsByEmail(loginRequest.getEmail())) {
            if (userRepository.existsByUsername(loginRequest.getUsername()))
                throw new ValidatorException("This username is already taken!");

            // зарегистрировать пользователя
            user = createUser(new SignupRequest(loginRequest.getUsername(), loginRequest.getEmail(), null, loginRequest.getAccessToken()));
            user.setFirstName(loginRequest.getFirstName());
            user.setSecondName(loginRequest.getSecondName());
            user.setIsEnabled(true);
            user.setStatus("oAuth2");
            userRepository.save(user);
        }
        else {
            user = userRepository.findByEmail(loginRequest.getEmail()).get();
            if (!user.getStatus().equals("oAuth2"))
                throw new ValidatorException("User already registered via platform registration. Please enter your username and password!");
        }

        user.setPassword(encoder.encode(loginRequest.getAccessToken()));
        userRepository.save(user);

        return authenticateUser(new LoginRequest(user.getUsername(), loginRequest.getAccessToken()));
    }

    public void registerUser(SignupRequest signUpRequest) throws ValidatorException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ValidatorException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ValidatorException("Error: Email is already in use!");
        }

        User user = createUser(signUpRequest);
        user.setStatus("EmailNotConfirmed");
        userRepository.save(user);

        ConfirmationToken token = confirmationTokenService.createToken(user);
        try {
            emailService.sendConfirmationMessage(user, token.getConfirmationToken());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private User createUser(SignupRequest request) {
        // Create new user's account
        User user = new User(request.getUsername(),
                encoder.encode(request.getPassword()),
                request.getEmail(), false);

        Set<String> strRoles = request.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new ValidatorException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new ValidatorException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new ValidatorException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new ValidatorException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    private boolean confirmGoogleToken(String accessToken, String userEmail) {
        ResponseEntity<Map> response = new RestTemplate().getForEntity(googleUrl + accessToken, Map.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            String email = response.getBody().getOrDefault("email", "").toString();
            return !(email.isEmpty()) && email.equals(userEmail);
        }
        return false;
    }

    @Transactional
    public void confirmUser(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getByConfirmationToken(token);
        if(confirmationToken != null){
            User user = userRepository.findByUsername(confirmationToken.getUser().getUsername()).get();

            user.setIsEnabled(true);
            user.setStatus("Confirmed");
            userRepository.save(user);
            confirmationTokenService.deleteByConfirmationToken(token);
        }

        else throw new ValidatorException("Confirmation error!");
    }
}