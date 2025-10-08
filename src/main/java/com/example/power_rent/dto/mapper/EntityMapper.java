package com.example.power_rent.dto.mapper;

import com.example.power_rent.model.Powerbank;
import com.example.power_rent.model.Rental;
import com.example.power_rent.model.Station;
import com.example.power_rent.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    default Long map(Rental rental) {
        return rental == null ? null : rental.getId();
    }

    default Long map(Station station) {
        return station == null ? null : station.getId();
    }

    default Long map(Powerbank powerbank) {
        return powerbank == null ? null : powerbank.getId();
    }

    default Long map(User user) {
        return user == null ? null : user.getId();
    }
}
