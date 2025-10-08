package com.example.power_rent.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String secret;
    @Value("${app.jwtExpirationMs}")
    private int tokenLifetime;
    @Value("${app.jwtRefreshExpirationMs}")
    private int refreshTokenLifetime;

    public String generateAccessToken(Authentication auth) {
        return generateAccessToken(auth, tokenLifetime);
    }
    public String generateRefreshToken(Authentication auth) {
        return generateAccessToken(auth, refreshTokenLifetime);
    }
    private String generateAccessToken(Authentication auth, int lifetime) {
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        Date now = new Date();
        Date expiringDate = new Date(now.getTime() + lifetime);

        return Jwts.builder()
                .subject(userPrincipal.getId().toString())
                .issuedAt(now)
                .expiration(expiringDate)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
            // TODO: exceptions
        } catch (Exception e) {
            return false;
        }
//        } catch (ExpiredJwtException e) {
//
//        } catch (UnsupportedJwtException e) {
//
//        } catch (MalformedJwtException e) {
//
//        } catch (SecurityException | SignatureException e) {
//
//        } catch (IllegalArgumentException e) {
//
//        }
    }

    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }
}
