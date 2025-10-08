package com.example.power_rent.dto;

import com.example.power_rent.enums.RentalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RentalDto {
    public static class Response {
        @Schema(name = "RentalResponseBasic")
        public record Basic (
                Long id,

                LocalDateTime startTime,

                LocalDateTime endTime,

                Long startStation,

                Long endStation,

                RentalStatus status,

                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
                BigDecimal rentPrice,

                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
                BigDecimal paymentAmount,

                Long user,

                Long powerbank
        ) {}
    }

    public static class Request {
        @Schema(name = "RentalRequestCreate")
        public record Create(
                Long stationId,
                Long userId
        ) {}
    }
}
