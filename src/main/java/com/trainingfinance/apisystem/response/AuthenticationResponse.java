package com.trainingfinance.apisystem.response;

import com.trainingfinance.apisystem.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private List<String> role;
    private Profile profile;
    private Long id;
}
