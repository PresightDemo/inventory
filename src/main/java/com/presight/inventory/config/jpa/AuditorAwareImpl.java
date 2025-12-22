package com.presight.inventory.config.jpa;

import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AuditorAwareImpl {


    @Bean
    public AuditorAware<String> auditorProvider() {
        //TODO:  In a real app, return logged-in user from SecurityContext
//        return () -> Optional.of("SYSTEM"); // Example: always "SYSTEM"

        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("SYSTEM_1");
            }
            // The username from Basic Auth
            return Optional.of(authentication.getName());
        };
    }

}

