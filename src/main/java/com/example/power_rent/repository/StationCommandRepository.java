package com.example.power_rent.repository;

import com.example.power_rent.enums.StationCommandStatus;
import com.example.power_rent.model.station_command.StationCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationCommandRepository extends JpaRepository<StationCommand, Long> {

    List<StationCommand> findByStationIdAndStatus(Long stationId, StationCommandStatus status);
}
