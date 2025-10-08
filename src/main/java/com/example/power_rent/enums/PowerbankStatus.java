package com.example.power_rent.enums;

public enum PowerbankStatus {
    AVAILABLE,  // sets by StationApi
    RESERVED,   // sets when creating Rental
    RENTED,     // sets when starting Rental
    CHARGING,   // sets after RECEIVE command
    MAINTENANCE
}
