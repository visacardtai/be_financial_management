package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.AuthenticationDto;
import com.trainingfinance.apisystem.entity.Account;
import com.trainingfinance.apisystem.repository.AccountRepository;
import com.trainingfinance.apisystem.response.AuthenticationResponse;
import com.trainingfinance.apisystem.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AccountRepository accountRepository;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(HttpServletResponse response,
                                                               @RequestBody AuthenticationDto request) {
        return ResponseEntity.ok(authenticationService.authenticate(request,response));
    }
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
