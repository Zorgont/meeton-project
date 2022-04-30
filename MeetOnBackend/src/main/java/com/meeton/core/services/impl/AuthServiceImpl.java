package com.meeton.core.services.impl;

import com.meeton.core.dto.SignupRequest;
import com.meeton.core.entities.ERole;
import com.meeton.core.entities.Role;
import com.meeton.core.entities.User;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.repositories.RoleRepository;
import com.meeton.core.repositories.UserRepository;
import com.meeton.core.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final private UserRepository userRepository;
    final private RoleRepository roleRepository;
    final private PasswordEncoder encoder;

    public void registerUser(SignupRequest signUpRequest) throws ValidatorException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ValidatorException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ValidatorException("Error: Email is already in use!");
        }

        User user = createUser(signUpRequest);
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
}