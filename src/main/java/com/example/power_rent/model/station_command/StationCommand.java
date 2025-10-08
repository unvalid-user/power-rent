package com.example.power_rent.model.station_command;

import com.example.power_rent.enums.StationCommandStatus;
import com.example.power_rent.service.RentalService;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DispenseCommand.class, name = "DISPENSE"),
        @JsonSubTypes.Type(value = ReceiveCommand.class, name = "RECEIVE"),
        @JsonSubTypes.Type(value = SyncCommand.class, name = "SYNC")
})
@Entity
@Table(name = "station_commands")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class StationCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_id", nullable = false)
    private Long stationId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StationCommandStatus status = StationCommandStatus.PENDING;

    public abstract void execute(RentalService rentalService);


    public StationCommand(Long stationId, StationCommandStatus status) {
        this.stationId = stationId;
        this.status = status;
    }

    public StationCommand(Long stationId) {
        this.stationId = stationId;
    }
}