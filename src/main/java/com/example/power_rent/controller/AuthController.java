package com.example.power_rent.controller;

import com.example.power_rent.dto.UserDto;
import com.example.power_rent.dto.mapper.UserMapper;
import com.example.power_rent.enums.UserRole;
import com.example.power_rent.exception.AccessDeniedException;
import com.example.power_rent.exception.BadRequestException;
import com.example.power_rent.model.User;
import com.example.power_rent.repository.UserRepository;
import com.example.power_rent.security.JwtAuthResponse;
import com.example.power_rent.security.JwtTokenProvider;
import com.example.power_rent.security.UserPrincipal;
import com.example.power_rent.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// TODO:
//  - add service layer
//  - exception handling
//  - refresh token table

// TODO-new-features:
//  - referral code & link

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> authenticateUser(
            @Valid @RequestBody UserDto.Request.Login loginRequest
    ) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.usernameOrEmail(),
                        loginRequest.password()));

        var authResponse = JwtAuthResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(auth))
                .refreshToken(jwtTokenProvider.generateRefreshToken(auth))
                .build();

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto.Response.Basic> registerUser(
            @Valid @RequestBody UserDto.Request.SignUp signUpRequest
    ) {
        User createdUser = userService.createUser(signUpRequest, UserRole.CUSTOMER);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(userMapper.toBasicResponse(createdUser));
    }

    @GetMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refreshTokens(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody String refreshToken
    ) {
        jwtTokenProvider.validateToken(refreshToken);

        // TODO: exception
        if (userPrincipal.getId() != jwtTokenProvider.getUserIdFromJwt(refreshToken))
            throw new AccessDeniedException();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var authResponse = JwtAuthResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(auth))
                .refreshToken(jwtTokenProvider.generateRefreshToken(auth))
                .build();

        return ResponseEntity.ok(authResponse);
    }
}
