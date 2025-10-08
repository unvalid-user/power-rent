package com.example.power_rent.repository;

import com.example.power_rent.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByCode(String code);

    Optional<Station> findByIdOrCode(@Nullable Long id, @Nullable String code);

}
