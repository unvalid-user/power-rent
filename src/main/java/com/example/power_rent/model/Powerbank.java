package com.example.power_rent.model;

import com.example.power_rent.enums.PowerbankStatus;
import com.example.power_rent.enums.PowerbankType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "powerbanks")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Powerbank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number", unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PowerbankType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PowerbankStatus status = PowerbankStatus.CHARGING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station currentStation;

    public Powerbank(String serialNumber, PowerbankType type) {
        this.serialNumber = serialNumber;
        this.type = type;
    }
}
