package com.example.power_rent.model;

import com.example.power_rent.enums.PowerbankType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "stations")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column
    private String name;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "powerbank_type", nullable = false)
    private PowerbankType powerbankType;

    @Column(name = "total_slots",nullable = false)
    private Integer totalSlots;

    // price per hour
    @Column(name = "rent_price", nullable = false)
    private BigDecimal rentPrice;

    @OneToMany(mappedBy = "id")
    private List<Powerbank> powerbanks;


    public Station(String name,
                   String location,
                   PowerbankType powerbankType,
                   Integer totalSlots,
                   BigDecimal rentPrice) {
        this.name = name;
        this.location = location;
        this.powerbankType = powerbankType;
        this.totalSlots = totalSlots;
        this.rentPrice = rentPrice;
    }
}
