package com.example.power_rent.service;

import com.example.power_rent.dto.StationDto;
import com.example.power_rent.enums.RentalStatus;
import com.example.power_rent.exception.AccessDeniedException;
import com.example.power_rent.exception.BadRequestException;
import com.example.power_rent.exception.ResourceNotFoundException;
import com.example.power_rent.model.Powerbank;
import com.example.power_rent.model.Rental;
import com.example.power_rent.model.Station;
import com.example.power_rent.model.User;
import com.example.power_rent.repository.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


// TODO-service:
//  - read about JpaRepository.flush() and etc.
//  - read about JpaRepository.save()
//  - exception handling
//  - Transactional


@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private StationService stationService;

    @Autowired
    private StationCommandService commandService;

    @Autowired
    private PowerbankService powerbankService;


    // TODO:
    //  - if user's balance < 0, rental should not start
    //  - possible le problemo:
    //      Station dispenses powerbank, but someone steals it

    @Transactional
    public Rental createRental(User user, StationDto.Request.Get stationRequest) {
        Station station = stationService.findStationByIdOrCode(stationRequest);
        return createRental(user, station);
    }
    public Rental createRental(User user, Station station) {
        Powerbank powerbank = powerbankService.reservePowerbank(station.getId());

        Rental rental = new Rental(
                station,
                user.getId(),
                powerbank
        );
        rental.setRentPrice(station.getRentPrice());
        rental.setStatus(RentalStatus.PENDING);

        commandService.createDispenseCommand(
                station.getId(),
                powerbank.getSerialNumber(),
                rental);

        return rentalRepository.save(rental);
    }

    public Rental startRental(Long rentalId) {
        return startRental(findRentalById(rentalId));
    }
    public Rental startRental(Rental rental) {
        // этот метод запускается автоматически, когда станция выполнит DispenseCommand
        // TODO
        powerbankService.rentPowerbank(rental.getPowerbank());

        rental.setStatus(RentalStatus.ACTIVE);
        rental.setStartTime(LocalDateTime.now());

        return rentalRepository.save(rental);
    }

    // TODO:
    //  - exception message
    //  - payment:
    //      fix calculation
    //      issue an invoice
    @Transactional
    public Rental completeRental(Long rentalId, StationDto.Request.Get stationRequest, User user) {
        Rental rental = findRentalById(rentalId);
        if (rental.getUserId() != user.getId())
            throw new AccessDeniedException();
        Station station = stationService.findStationByIdOrCode(stationRequest);
        if (station.getPowerbankType() != rental.getPowerbank().getType())
            throw new BadRequestException("Wrong powerbank type");

        commandService.createReceiveCommand(station.getId(), rental.getPowerbank().getSerialNumber(), rental);

        rental.setEndStation(station);
        return rentalRepository.save(rental);
    }

    public BigDecimal calculatePayment(Rental rental) {
        long diff = ChronoUnit.HOURS.between(rental.getStartTime(), rental.getEndTime());
        return rental.getRentPrice().multiply(BigDecimal.valueOf(diff));
    }

    public void finishRental(Rental rental, Long stationId) {
        // этот метод запускается автоматически, когда станция выполнит ReceiveCommand
        // TODO
        Station station = stationService.findStationById(stationId);
        powerbankService.returnPowerbankToStation(rental.getPowerbank(), station);

        rental.setEndTime(LocalDateTime.now());
        rental.setPaymentAmount(calculatePayment(rental));
        rental.setStatus(RentalStatus.COMPLETED);

        rentalRepository.save(rental);
    }

    public List<Rental> findRentalsByUserId(Long userId) {
        return rentalRepository.findByUserId(userId);
    }

    public Rental findRentalById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", "id", id));
    }

    // TODO:
    //  - add sort by date
    //  - Specification (JPA Criteria API)
    //  - exception if empty?
    public Page<Rental> getRentals(int page, int size, Long userId, RentalStatus status) {
        if (status != null) {
            return rentalRepository.findByUserIdAndStatus(PageRequest.of(page, size), userId, status);
        }
        return rentalRepository.findByUserId(PageRequest.of(page, size), userId);
    }
}
