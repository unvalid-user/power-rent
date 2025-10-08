package com.example.power_rent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// station should:
// 1. automatically set powerbanks status to CHARGED
// 2. request commands type list:
//      - DISPENSE pb
//      - RECEIVE pb
//      - SYNC
// 3. notify if command have been completed
// 4. send pb list for syncing

@RestController
@RequestMapping("/api/station")
public class StationApiController {



    @PatchMapping("/powerbanks/{id}")
    public void setPowerbankStatusToAvailable() {}

    @GetMapping("/commands")
    public void getCommands() {}

    @PatchMapping("/commands/{id}")
    public void completeCommand() {}

    // TODO: log if station's data differs from DB's data
    @PatchMapping("/powerbanks")
    public void syncPowerbanksData() {}

}
