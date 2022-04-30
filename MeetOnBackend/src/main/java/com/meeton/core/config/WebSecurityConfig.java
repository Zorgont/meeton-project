package com.meeton.core.config;

import com.meeton.core.entities.*;
import com.meeton.core.repositories.RoleRepository;
import com.meeton.core.repositories.TagRepository;
import com.meeton.core.repositories.UserRepository;
import com.meeton.core.security.AuthEntryPointJwt;
import com.meeton.core.security.AuthTokenFilter;
import com.meeton.core.services.PlatformService;
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

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/meeton-core/v1/auth/**").permitAll()
                .antMatchers("/meeton-core/v1/tags/**").permitAll()
                .antMatchers("/meeton-core/v1/platforms/**").permitAll()
                .antMatchers(HttpMethod.GET, "/meeton-core/v1/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/meeton-core/v1/meetings/**").permitAll()
                .antMatchers(HttpMethod.GET, "/meeton-core/v1/score/**").permitAll()
                .antMatchers("/meeton-core/v1/score/**").authenticated()
                .antMatchers("/meeton-core/v1/users/**").authenticated()
                .antMatchers("/meeton-core/v1/meetings/**").authenticated()
                .antMatchers("/meeton-core/v1/requests/**").authenticated()
                .antMatchers(HttpMethod.GET, "/meeton-core/v1/comments/**").permitAll()
                .antMatchers("/meeton-core/v1/comments/**").authenticated()
                .antMatchers("/ws/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}