package com.trainingfinance.apisystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingfinance.apisystem.dto.AuthenticationDto;
import com.trainingfinance.apisystem.entity.*;
import com.trainingfinance.apisystem.repository.*;
import com.trainingfinance.apisystem.response.AuthenticationResponse;
import com.trainingfinance.apisystem.token.Token;
import com.trainingfinance.apisystem.token.TokenRepository;
import com.trainingfinance.apisystem.token.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final ProfileRepository profileRepository;
//    public AuthenticationResponse authenticate(AuthenticationDto request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );
//        var user = accountRepository.findByUsername(request.getUsername()).orElseThrow();
//        List<String> roleSet = user.getAccountRoleEntities().stream().map(role -> role.getRole().getName()).collect(Collectors.toList());
//        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse
//                .builder()
//                .token(jwtToken)
//                .role(roleSet)
//                .build();
//    }
public AuthenticationResponse authenticate(AuthenticationDto request, HttpServletResponse response) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
    );
    var user = accountRepository.findByUsername(request.getUsername()).orElseThrow();
    Profile profile = new Profile();
    long id = 0;
    if(user.getAccountId() != null) {
        try {
            Student student = studentRepository.findByAccountId(user.getAccountId());
            id = student.getStudentId();
            profile = student.getProfile();
        } catch (Exception e) {
            try {
                Lecturer lecturer = lecturerRepository.findByAccountId(user.getAccountId());
                id = lecturer.getLecturerId();
                profile = lecturer.getProfile();
            } catch (Exception a) {
                try {
                    Employee employee = employeeRepository.findByAccountId(user.getAccountId());
                    id = employee.getEmployeeId();
                    profile = employee.getProfile();
                } catch (Exception b){
                }
            }
        }
    }
    var jwtToken = jwtService.generateToken(user);
    List<String> roleSet = user.getAccountRoleEntities().stream().map(role -> role.getRole().getName()).collect(Collectors.toList());
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    Cookie cookie = new Cookie("refresh_token", refreshToken);
    cookie.setMaxAge(36000);
    cookie.setPath("/");
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
    System.out.println(jwtToken);
    return AuthenticationResponse.builder()
            .id(id)
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .role(roleSet)
            .profile(profile)
            .build();
}

    private void saveUserToken(Account account, String jwtToken) {
        var token = Token.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Account account) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(account.getAccountId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String authHeader = null;
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")){
                    authHeader = cookie.getValue();
                }
            }
        } else {
            return;
        }

        final String refreshToken;
        final String userEmail;
        if (authHeader == null) {
            return;
        }
//        || !authHeader.startsWith("Bearer ")
        refreshToken = authHeader;
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = this.accountRepository.findByUsername(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                List<String> roleSet = user.getAccountRoleEntities().stream().map(role -> role.getRole().getName()).collect(Collectors.toList());
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .role(roleSet)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
