package com.trainingfinance.apisystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

import static com.trainingfinance.apisystem.config.Roles.ADMIN;
import static com.trainingfinance.apisystem.config.Roles.USER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/api/v1/public",
            "/api/v1/public/**"};

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CorsFilter corsFilter;
    private final LogoutHandler logoutHandler;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays
                .asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @ConditionalOnMissingBean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .addFilterBefore(corsFilter, SessionManagementFilter.class)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/api/v1/student/student-expenses/").hasAnyAuthority("ROLE_SINHVIEN")
                                .requestMatchers("/api/v1/student").hasAnyAuthority("ROLE_SINHVIEN")
                                .requestMatchers("/api/v1/student/**").hasAnyAuthority("ROLE_SINHVIEN")
                                .requestMatchers("/api/v1/lecturer").hasAnyAuthority("ROLE_GIANGVIEN")
                                .requestMatchers("/api/v1/lecturer/lecture-price").hasAnyAuthority("ROLE_TRUONGPHONG","ROLE_QUANLY", "ROLE_GIANGVIEN")
                                .requestMatchers("/api/v1/lecturer/**").permitAll()
                                .requestMatchers("/api/v1/expert/targets").hasAnyAuthority("ROLE_GIANGVIEN","ROLE_TRUONGPHONG","ROLE_QUANLY")
                                .requestMatchers("/api/v1/expert/class-coefficient").hasAnyAuthority("ROLE_GIANGVIEN","ROLE_TRUONGPHONG","ROLE_QUANLY")
                                .requestMatchers("/api/v1/expert/**").hasAnyAuthority("ROLE_TRUONGPHONG","ROLE_QUANLY")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
        );

        return http.build();
    }
}




//.requestMatchers("/api/v1/expert").hasAnyRole("QUANLY", "TRUONGPHONG")
//        .requestMatchers("/api/v1/expert/**").hasAnyRole("QUANLY", "TRUONGPHONG")
//        .requestMatchers("/api/v1/lecturer").hasAnyRole("QUANLY", "TRUONGPHONG","GIANGVIEN")
//        .requestMatchers("/api/v1/lecturer/**").hasAnyRole("QUANLY", "TRUONGPHONG","GIANGVIEN")
//        .requestMatchers("/api/v1/student").hasRole("SINHVIEN")
//hasAnyAuthority("ROLE_TRUONGPHONG","ROLE_QUANLY")