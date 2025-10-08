package com.example.power_rent.service;

import com.example.power_rent.enums.PowerbankStatus;
import com.example.power_rent.exception.ConflictException;
import com.example.power_rent.exception.ResourceNotFoundException;
import com.example.power_rent.model.Powerbank;
import com.example.power_rent.model.Station;
import com.example.power_rent.repository.PowerbankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PowerbankService {
    @Autowired
    private PowerbankRepository powerbankRepository;


    public Powerbank reservePowerbank(Long stationId) {
        Powerbank powerbank = powerbankRepository
                .findFirstByCurrentStation_IdAndStatus(stationId, PowerbankStatus.AVAILABLE)
                .orElseThrow(() -> new ConflictException("No available powerbanks"));

        powerbank.setStatus(PowerbankStatus.RESERVED);

        return powerbankRepository.save(powerbank);
    }

    public Powerbank rentPowerbank(Long powerbankId) {
        Powerbank powerbank = getPowerbankById(powerbankId);
        return rentPowerbank(powerbank);
    }
    public Powerbank rentPowerbank(Powerbank powerbank) {
        powerbank.setCurrentStation(null);
        powerbank.setStatus(PowerbankStatus.RENTED);
        return powerbankRepository.save(powerbank);
    }

    public Powerbank returnPowerbankToStation(Long powerbankId, Station station) {
        return returnPowerbankToStation(getPowerbankById(powerbankId), station);
    }
    public Powerbank returnPowerbankToStation(Powerbank powerbank, Station station) {
        powerbank.setCurrentStation(station);
        powerbank.setStatus(PowerbankStatus.CHARGING);
        return powerbankRepository.save(powerbank);
    }

    public Powerbank getPowerbankById(Long id) {
        return powerbankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Powerbank", "id", id));
    }
}
