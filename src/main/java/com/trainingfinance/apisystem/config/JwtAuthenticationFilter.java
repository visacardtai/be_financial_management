package com.trainingfinance.apisystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingfinance.apisystem.entity.Role;
import com.trainingfinance.apisystem.repository.AccountRepository;
import com.trainingfinance.apisystem.repository.RoleRepository;
import com.trainingfinance.apisystem.response.AuthenticationResponse;
import com.trainingfinance.apisystem.service.JwtService;
import com.trainingfinance.apisystem.token.TokenRepository;
import com.trainingfinance.apisystem.utils.AccountHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("Nguye ntie ntai");
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String userName = null;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7);
        try {
            userName = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            if (e.getMessage().equals("rf_ex")) {

                if (request.getServletPath().contains("refresh-token")) {
                    filterChain.doFilter(request,response);
                    return;
                }
                response.setStatus(417);
                //response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getWriter(), null);
                filterChain.doFilter(request,response);
                return;
            }

        }
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            System.out.println("ngt " + userDetails.getAuthorities());
            if(jwtService.isTokenValid(jwt,userDetails) && isTokenValid){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
