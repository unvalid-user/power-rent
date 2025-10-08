package com.example.power_rent.model.station_command;

import com.example.power_rent.enums.StationCommandStatus;
import com.example.power_rent.model.Rental;
import com.example.power_rent.model.Station;
import com.example.power_rent.service.RentalService;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "receive_commands")
@NoArgsConstructor
public class ReceiveCommand extends StationCommand {
    @Column(name = "powerbank_serial", nullable = false)
    private String powerbankSerial;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    @Override
    public void execute(RentalService rentalService) {
        rentalService.finishRental(rental, super.getStationId());
    }

    public ReceiveCommand(Long stationId, String powerbankSerial, Rental rental) {
        super(stationId);
        this.powerbankSerial = powerbankSerial;
        this.rental = rental;
    }
}
