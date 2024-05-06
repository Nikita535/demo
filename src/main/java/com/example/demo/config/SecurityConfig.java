package com.example.demo.config;

import com.example.demo.config.jwt.JwtAuthFilter;
import com.example.demo.config.jwt.JwtUtil;
import com.example.demo.service.impl.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationEntryPoint authenticationEntryPoint;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(
                        httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                                .frameOptions(HeadersConfigurer
                                        .FrameOptionsConfig::disable)
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .authenticationEntryPoint(authenticationEntryPoint))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {authorizationManagerRequestMatcherRegistry
                        .requestMatchers("**","/swagger-ui/**",
                                "/swagger-resources/*",
                                "/v3/api-docs/**","/api/media/**","/api/ws/**").permitAll()
                        .anyRequest().authenticated();})
                .addFilterBefore(new JwtAuthFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
