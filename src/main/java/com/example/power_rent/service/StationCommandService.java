package com.example.power_rent.service;

import com.example.power_rent.enums.StationCommandStatus;
import com.example.power_rent.exception.ResourceNotFoundException;
import com.example.power_rent.model.Rental;
import com.example.power_rent.model.Station;
import com.example.power_rent.model.station_command.DispenseCommand;
import com.example.power_rent.model.station_command.ReceiveCommand;
import com.example.power_rent.model.station_command.StationCommand;
import com.example.power_rent.model.station_command.SyncCommand;
import com.example.power_rent.repository.StationCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationCommandService {
    @Autowired
    private StationCommandRepository commandRepository;


    public StationCommand createDispenseCommand(Long stationId, String powerbankSerial, Rental rental) {
        return commandRepository.save(new DispenseCommand(
                stationId,
                powerbankSerial,
                rental
        ));
    }

    public StationCommand createReceiveCommand(Long stationId, String powerbankSerial, Rental rental) {
        return commandRepository.save(new ReceiveCommand(
                stationId,
                powerbankSerial,
                rental
        ));
    }

    public StationCommand createSyncCommand(Long stationId) {
        return commandRepository.save(new SyncCommand(
                stationId
        ));
    }

    public List<StationCommand> getPendingCommandsByStationOd(Long stationId) {
        return commandRepository.findByStationIdAndStatus(stationId, StationCommandStatus.PENDING);
    }
}
