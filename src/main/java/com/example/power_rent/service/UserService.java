package com.example.power_rent.service;

import com.example.power_rent.dto.UserDto;
import com.example.power_rent.enums.UserRole;
import com.example.power_rent.exception.BadRequestException;
import com.example.power_rent.model.User;
import com.example.power_rent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(UserDto.Request.SignUp signUpRequest, UserRole role) {
        if (userRepository.existsByEmail(signUpRequest.email()))
            throw new BadRequestException("Email is already in use");

        User user = new User(
                signUpRequest.email(),
                signUpRequest.email(),
                passwordEncoder.encode(signUpRequest.password()),
                role
        );
        return userRepository.save(user);
    }
}
