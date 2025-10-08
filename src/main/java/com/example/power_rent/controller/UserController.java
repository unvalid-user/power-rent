package com.example.power_rent.controller;

import com.example.power_rent.dto.UserDto;
import com.example.power_rent.dto.mapper.UserMapper;
import com.example.power_rent.security.UserPrincipal;
import com.example.power_rent.service.UserService;
import io.swagger.v3.oas.annotations.headers.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

// TODO-admin: add endpoints for ADMIN-role (or/and for SUPPORT-role)

// TODO:
//  - username change format:
//      by default username == email while registering user
//      and user should not be able to change their username to someone's email

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserDto.Response.Basic> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return ResponseEntity.ok(userMapper.toBasicResponse(userPrincipal.getUser()));
    }

    @GetMapping("/sas")
    public void sas(Authentication authentication) {
        System.out.println(authentication.getPrincipal());
    }
}
