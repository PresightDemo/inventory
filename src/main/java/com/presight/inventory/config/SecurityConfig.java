package com.presight.inventory.config;

import com.presight.common_lib.security.web.BaseWebSecurityFilterChainFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig  {

    @Bean
    public BaseWebSecurityFilterChainFactory securityFilterChainFactory() {
        return new BaseWebSecurityFilterChainFactory();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, BaseWebSecurityFilterChainFactory factory) throws Exception {
        factory.configureCommonSecurity(httpSecurity);
        return httpSecurity.build();
    }
}
