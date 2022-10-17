package com.avrioc.student.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Class used only with Junit
 * Used for disabling http csrf as spring security is enabled.
 */

@Configuration
@EnableWebFluxSecurity
@Profile("test")
public class WebFluxSecurityConfig {

    /**
     * Bean that returns SecurityWebFilterChain with csrf disabled.
     *
     * @param http
     * @return SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable().build();
    }
}