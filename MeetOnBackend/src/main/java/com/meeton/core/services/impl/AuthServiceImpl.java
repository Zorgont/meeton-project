package com.meeton.core.services.impl;

import com.meeton.core.dto.JwtResponse;
import com.meeton.core.dto.LoginRequest;
import com.meeton.core.dto.SignupRequest;
import com.meeton.core.entities.ConfirmationToken;
import com.meeton.core.entities.ERole;
import com.meeton.core.entities.Role;
import com.meeton.core.entities.User;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.repositories.RoleRepository;
import com.meeton.core.repositories.UserRepository;
import com.meeton.core.security.JwtUtils;
import com.meeton.core.services.AuthService;
import com.meeton.core.services.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final private AuthenticationManager authenticationManager;
    final private UserRepository userRepository;
    final private RoleRepository roleRepository;
    final private ConfirmationTokenService confirmationTokenService;
    final private PasswordEncoder encoder;
    final private JwtUtils jwtUtils;

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

    public void registerUser(SignupRequest signUpRequest) throws ValidatorException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ValidatorException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ValidatorException("Error: Email is already in use!");
        }

        User user = createUser(signUpRequest);
//        if (signUpRequest.getProvider().equals("platform")) {
//            user.setStatus("EmailNotConfirmed");
//
//            ConfirmationToken token = confirmationTokenService.createToken(user);
//            try {
//                emailService.sendConfirmationMessage(user, token.getConfirmationToken());
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        } else {
//            user.setIsEnabled(true);
//            user.setStatus("Confirmed");
//        }
        user.setIsEnabled(true);
        user.setStatus("Confirmed");
        userRepository.save(user);
    }

    private User createUser(SignupRequest request) {
        // Create new user's account

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .isEnabled(false)
                .build();
        if (!StringUtils.isEmpty(request.getPassword())) {
            user.setPassword(encoder.encode(request.getPassword()));
        }

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