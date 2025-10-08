package com.example.power_rent.controller;

import com.example.power_rent.dto.RentalDto;
import com.example.power_rent.dto.mapper.RentalMapper;
import com.example.power_rent.model.Rental;
import com.example.power_rent.security.UserPrincipal;
import com.example.power_rent.service.RentalService;
import com.example.power_rent.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {
    @Autowired
    private StationService stationService;

    @Autowired
    private RentalMapper rentalMapper;


    // TODO
    @PostMapping("/{id}/reserve")
    public ResponseEntity<RentalDto.Response.Basic> reservePowerbank(
            @PathVariable(name = "id") Long stationId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        // пользователь резервирует павербарк,
        // чтобы его можно было гарантированно забрать, когда он подойдет к станции
        // система создает аренду со статусом PENDING

        Rental createdRental = stationService.reservePowerbank(userPrincipal.getUser(), stationId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/rentals/{id}")
                .buildAndExpand(createdRental.getId())
                .toUri();
        return ResponseEntity.created(location).body(rentalMapper.toBasicResponse(createdRental));
    }



}
