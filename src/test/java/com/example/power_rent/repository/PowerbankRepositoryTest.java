package com.example.power_rent.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PowerbankRepositoryTest {
    @Autowired
    private PowerbankRepository powerbankRepository;


    @BeforeEach
    void beforeEach() {
    }

    @Test
    void findPowerbankByStationAndStatus() {

    }
}
