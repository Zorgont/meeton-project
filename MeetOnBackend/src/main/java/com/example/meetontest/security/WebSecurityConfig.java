package com.example.meetontest.security;

import com.example.meetontest.entities.*;
import com.example.meetontest.repositories.RatingWeightRepository;
import com.example.meetontest.repositories.RoleRepository;
import com.example.meetontest.repositories.TagRepository;
import com.example.meetontest.repositories.UserRepository;
import com.example.meetontest.services.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TagRepository tagRepository;
    private final RatingWeightRepository ratingWeightRepository;
    private final PlatformService platformService;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_USER));
            roleRepository.save(new Role(ERole.ROLE_MODERATOR));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }

        if (tagRepository.count() == 0) {
            tagRepository.save(new Tag("Education"));
            tagRepository.save(new Tag("Programming"));
            tagRepository.save(new Tag("Java"));
            tagRepository.save(new Tag("Spring"));
            tagRepository.save(new Tag("Database"));
            tagRepository.save(new Tag("Cooking"));
            tagRepository.save(new Tag("Italian food"));
            tagRepository.save(new Tag("Investing"));
            tagRepository.save(new Tag("Money"));
            tagRepository.save(new Tag("Music"));
            tagRepository.save(new Tag("Guitar"));
        }

        if (platformService.getAll().size() == 0) {
            platformService.create(new Platform("Zoom", "Meeting platform", PlatformType.ONLINE));
            platformService.create(new Platform("Discord", "Network for gamers", PlatformType.ONLINE));
            platformService.create(new Platform("Skype", "Only for boomers", PlatformType.ONLINE));
            platformService.create(new Platform("Microsoft Teams", "Idk what is this", PlatformType.ONLINE));
        }

        if (!userRepository.existsByUsername("Zorgont")) {
            User vladlen = new User();
            vladlen.setUsername("Zorgont");
            vladlen.setFirstName("Vladlen");
            vladlen.setSecondName("Plakhotnyuk");
            vladlen.setEmail("zorgont@gmail.com");
            vladlen.setIsEnabled(true);
            vladlen.setAbout("Hello, I'm Vladlen!");
            vladlen.setStatus("active");
            vladlen.setPassword(passwordEncoder().encode("123456"));
            vladlen.setRoles(new HashSet<>(roleRepository.findAll()));
            userRepository.save(vladlen);
        }

        if (ratingWeightRepository.count() == 0){
            ratingWeightRepository.save(new RatingWeight(RatingWeightType.MEETING_CREATION,20.0));
            ratingWeightRepository.save(new RatingWeight(RatingWeightType.MEETING_ATTENDANCE, 1.0));
            ratingWeightRepository.save(new RatingWeight(RatingWeightType.MEETING_SCORE_COEFFICIENT, 400.0));
        }

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/tags/**").permitAll()
                .antMatchers("/api/v1/platforms/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/meetings/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/score/**").permitAll()
                .antMatchers("/api/v1/score/**").authenticated()
                .antMatchers("/api/v1/users/**").authenticated()
                .antMatchers("/api/v1/meetings/**").authenticated()
                .antMatchers("/api/v1/requests/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
                .antMatchers("/api/v1/comments/**").authenticated()
                .antMatchers("/api/v1/notifications/**").authenticated()
                .antMatchers("/ws/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}