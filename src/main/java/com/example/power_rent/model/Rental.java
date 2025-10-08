package com.example.power_rent.model;

import com.example.power_rent.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_station_id", nullable = false)
    private Station startStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_station_id")
    private Station endStation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RentalStatus status = RentalStatus.ACTIVE;

    @Column(name = "rent_price", nullable = false)
    private BigDecimal rentPrice;

    // TODO-payment_model: add payments table
    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "powerbank_id")
    private Powerbank powerbank;

    public Rental(Station startStation, Long userId, Powerbank powerbank) {
        this.startStation = startStation;
        this.userId = userId;
        this.powerbank = powerbank;
    }
}
