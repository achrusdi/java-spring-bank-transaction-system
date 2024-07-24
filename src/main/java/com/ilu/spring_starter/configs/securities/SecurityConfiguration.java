package com.ilu.spring_starter.configs.securities;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ilu.spring_starter.apis.errors.CustomAccessDeniedHandler;
import com.ilu.spring_starter.apis.errors.CustomAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

        private final AuthTokenFilter authTokenFilter;
        private final CustomAccessDeniedHandler customAccessDeniedHandler;
        private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

        private final Map<String, String[]> ENDPOINTS = Map.of(
                        "WHITE_LIST", new String[] {
                                        // "/swagger-ui/index.html",
                                        // "/v3/api-docs/**",
                                        // "/swagger-ui.html/**",
                                        // "/swagger-ui/**",
                                        "/api/v1/auth/**",
                        },
                        "ROLE_GENERAL", new String[] {
                                        "/api/v1/generals/**",
                        },
                        "ROLE_ADMIN", new String[] {
                                        "/api/v1/bank-accounts/**",
                                        "/api/v1/roles/**",
                                        "/api/v1/admin/**",
                                        "/api/v1/users/**",
                        });

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(
                                                httpSecuritySessionManagementConfigure -> httpSecuritySessionManagementConfigure
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(req -> req
                                                .requestMatchers(ENDPOINTS.get("WHITE_LIST"))
                                                .permitAll()
                                                .requestMatchers(ENDPOINTS.get("ROLE_GENERAL"))
                                                .hasAnyAuthority("ROLE_GENERAL")
                                                .requestMatchers(ENDPOINTS.get("ROLE_ADMIN"))
                                                .hasAnyAuthority("ROLE_ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/v1/transactions")
                                                .hasAnyAuthority("ROLE_ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/v1/transactions")
                                                .hasAnyAuthority("ROLE_GENERAL")
                                                .requestMatchers(HttpMethod.GET, "/api/v1/transactions/{id}, /api/v1/transactions/user/all")
                                                .hasAnyAuthority("ROLE_GENERAL")
                                                .anyRequest()
                                                .authenticated())
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                                .accessDeniedHandler(customAccessDeniedHandler))
                                .addFilterBefore(authTokenFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }
}