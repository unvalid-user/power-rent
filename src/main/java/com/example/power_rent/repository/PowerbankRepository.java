package com.example.power_rent.repository;

import com.example.power_rent.enums.PowerbankStatus;
import com.example.power_rent.model.Powerbank;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// TODO
//  - concurrent transactions conflict test

@Repository
public interface PowerbankRepository extends JpaRepository<Powerbank, Long> {
    // TODO
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Powerbank> findFirstByCurrentStation_IdAndStatus(Long id, PowerbankStatus status);
}