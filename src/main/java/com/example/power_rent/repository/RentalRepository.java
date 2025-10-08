package com.example.power_rent.repository;

import com.example.power_rent.enums.RentalStatus;
import com.example.power_rent.model.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserId(Long userId);
    Page<Rental> findByUserIdAndStatus(Pageable pageable, Long userId, RentalStatus status);
    Page<Rental> findByUserId(Pageable pageable, Long userId);
}
