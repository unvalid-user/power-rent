package com.example.power_rent.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StationDto {
    public static class Response {
    }
    public static class Request {
        @Schema(name = "StationRequestGet")
        public record Get(
            Long id,
            String code
        ) {}
    }
}
