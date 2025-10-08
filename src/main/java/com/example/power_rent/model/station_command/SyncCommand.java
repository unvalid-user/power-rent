package com.example.power_rent.model.station_command;

import com.example.power_rent.enums.StationCommandStatus;
import com.example.power_rent.model.Station;
import com.example.power_rent.service.RentalService;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "sync_commands")
@NoArgsConstructor
public class SyncCommand extends StationCommand{
    @Override
    public void execute(RentalService rentalService) {
        // do nothing
    }

    public SyncCommand(Long stationId) {
        super(stationId);
    }
}
