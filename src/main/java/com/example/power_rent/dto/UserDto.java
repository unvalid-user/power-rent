package com.example.power_rent.dto;

import com.example.power_rent.enums.UserRole;
import com.example.power_rent.model.Rental;
import com.example.power_rent.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class UserDto {
    public static class Response {
        @Schema(name = "UserResponseBasic")
        public record Basic(
                Long id,

                String email,

                String username,

                String role,

                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
                BigDecimal balance
        ) {}
    }


    public static class Request {
        @Schema(name = "UserRequestSignUp")
        public record SignUp(
                @NotBlank(message = "Email cannot be blank")
                @Email(message = "Incorrect email format")
                @Size(max = 50, message = "Email is too long")
                String email,

                @NotBlank(message = "Password cannot be blank")
                @Size(min = 6, max = 20, message = "Incorrect password format")
                String password,

                UserRole role
        ) {}
        @Schema(name = "UserRequestSignIn")
        public record Login(
                @NotBlank
                String usernameOrEmail,

                @NotBlank
                String password
        ) {}
    }
}
