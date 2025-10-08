package com.example.power_rent.service;

import com.example.power_rent.enums.StationCommandStatus;
import com.example.power_rent.exception.ResourceNotFoundException;
import com.example.power_rent.model.station_command.StationCommand;
import com.example.power_rent.repository.StationCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationApiService {
    @Autowired
    private StationCommandRepository commandRepository;

    @Autowired
    private RentalService rentalService;

    public void completeCommand(Long commandId) {
        StationCommand command = commandRepository.findById(commandId)
                .orElseThrow(() -> new ResourceNotFoundException("StationCommand", "id", commandId));

        command.setStatus(StationCommandStatus.COMPLETED);
        commandRepository.save(command);

        command.execute(rentalService);
    }
}
