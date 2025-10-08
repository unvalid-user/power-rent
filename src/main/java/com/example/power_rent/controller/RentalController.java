package com.example.power_rent.controller;

import com.example.power_rent.dto.PagedResponse;
import com.example.power_rent.dto.RentalDto;
import com.example.power_rent.dto.StationDto;
import com.example.power_rent.dto.mapper.RentalMapper;
import com.example.power_rent.enums.RentalStatus;
import com.example.power_rent.model.Rental;
import com.example.power_rent.security.UserPrincipal;
import com.example.power_rent.service.RentalService;
import com.example.power_rent.utils.Const;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// TODO: read information about:
//  - threads conflict: @Transactional, SELECT FOR UPDATE, Serializable
//  - difference between jakarta.transactional and spring transactional
//  - @Embedded


// TODO:
//  - add logs

@RestController
@RequestMapping("/rentals")
public class RentalController {
    @Autowired
    private RentalMapper rentalMapper;

    @Autowired
    private RentalService rentalService;

    // TODO authentication
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto.Response.Basic> getRentalById(@PathVariable("id") Long rentalId) {
        Rental rental = rentalService.findRentalById(rentalId);
        return ResponseEntity.status(HttpStatus.OK).body(rentalMapper.toBasicResponse(rental));
    }

    // TODO
    @PatchMapping("/{id}/start")
    public ResponseEntity<RentalDto.Response.Basic> startRental() {
        // тут предполагается, что у пользователя уже есть аренда с зарезервированным павербанком
        // пользователь вводит номер станции и получает павербанк
        // после выдачи павербанка данные аренды обновляются (статус и время)
        return null;
    }

    @PostMapping
    public ResponseEntity<RentalDto.Response.Basic> quickStartRental(
            @RequestBody StationDto.Request.Get stationRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Rental createdRental = rentalService.createRental(userPrincipal.getUser(), stationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/rentals/{id}")
                .buildAndExpand(createdRental.getId())
                .toUri();
        return ResponseEntity.created(location).body(rentalMapper.toBasicResponse(createdRental));
    }


    @PatchMapping("/{id}/complete")
    public ResponseEntity<RentalDto.Response.Basic> completeRental(
            @PathVariable("id") Long rentalId,
            @RequestBody StationDto.Request.Get stationRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Rental rental = rentalService.completeRental(rentalId, stationRequest, userPrincipal.getUser());

        return ResponseEntity.ok(rentalMapper.toBasicResponse(rental));
    }

    // TODO
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<RentalDto.Response.Basic> cancelRental() {
        return null;
    }

    @PageableAsQueryParam
    @GetMapping
    public ResponseEntity<PagedResponse<RentalDto.Response.Basic>> getAllRentals (
            @RequestParam(name = "page", defaultValue = Const.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", defaultValue = Const.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "status", required = false) RentalStatus status,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long userId = userPrincipal.getId();
        Page<Rental> rentals = rentalService.getRentals(page, size, userId, status);

        // TODO: mapper.toPagedResponse
        return ResponseEntity.ok(rentalMapper.toBasicPagedResponse(rentals));
    }
}
