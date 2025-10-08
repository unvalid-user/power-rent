package com.example.power_rent.service;

import com.example.power_rent.dto.StationDto;
import com.example.power_rent.enums.PowerbankStatus;
import com.example.power_rent.exception.BadRequestException;
import com.example.power_rent.exception.ConflictException;
import com.example.power_rent.exception.ResourceNotFoundException;
import com.example.power_rent.model.Powerbank;
import com.example.power_rent.model.Rental;
import com.example.power_rent.model.Station;
import com.example.power_rent.model.User;
import com.example.power_rent.repository.PowerbankRepository;
import com.example.power_rent.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;


    @Transactional
    public Rental reservePowerbank(User user, Long stationId) {

        // TODO ...

        return null;
    }

    public Station findStationByIdOrCode(StationDto.Request.Get stationRequest) {
        return findStationByIdOrCode(stationRequest.id(), stationRequest.code());
    }
    public Station findStationByIdOrCode(Long id, String code) {
        return stationRepository.findByIdOrCode(id, code)
                .orElseThrow(() -> code == null ?
                        new ResourceNotFoundException("Station", "id", id) :
                        new ResourceNotFoundException("Station", "code", code)
                );
    }

    public Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new ResourceNotFoundException("Station", "id", stationId));
    }

    public Station findStationByCode(String stationCode) {
        return stationRepository.findByCode(stationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Station", "code", stationCode));
    }
}
