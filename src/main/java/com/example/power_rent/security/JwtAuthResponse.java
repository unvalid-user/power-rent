package com.example.power_rent.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthResponse {
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType = "Bearer";

    public JwtAuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
