package com.presight.inventory.config;

import com.presight.common_lib.security.LocalUsersProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(LocalUsersProperties.class)
public class YamlUserDetailsService implements UserDetailsService {

    private final LocalUsersProperties localUsersProperties;
    private final PasswordEncoder passwordEncoder; // injected

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return localUsersProperties.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .map(u -> User.withUsername(u.getUsername())
                        .password(passwordEncoder.encode(u.getPassword()))
                        .roles(u.getRoles().split(","))
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}

