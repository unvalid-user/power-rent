package com.example.power_rent.E2E;

import com.example.power_rent.dto.PagedResponse;
import com.example.power_rent.dto.RentalDto;
import com.example.power_rent.dto.StationDto;
import com.example.power_rent.dto.UserDto;
import com.example.power_rent.enums.StationCommandStatus;
import com.example.power_rent.model.Rental;
import com.example.power_rent.model.User;
import com.example.power_rent.model.station_command.StationCommand;
import com.example.power_rent.repository.RentalRepository;
import com.example.power_rent.repository.StationCommandRepository;
import com.example.power_rent.repository.UserRepository;
import com.example.power_rent.security.JwtAuthResponse;
import com.example.power_rent.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


// TODO:
//  - multithreading test:
//      several users trying to start Rental at the same time on the same Station

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RentalTest {
    @LocalServerPort
    private int port;
    private String apiURL;

    private User userCustomer, userAdmin;
    private HttpHeaders customerHeaders = new HttpHeaders();
    private HttpHeaders adminHeaders = new HttpHeaders();

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private StationCommandRepository commandRepository;

    @Autowired
    private RentalService rentalService;

    @BeforeEach
    public void beforeEach() {
        apiURL = "http://localhost:" + port;

        String customerPassword = "customer-password";
        String adminPassword = "admin-password";

        userCustomer = userRepository.findById(1L).get();
        userAdmin = userRepository.findById(2L).get();

        userCustomer.setPassword(customerPassword);
        userAdmin.setPassword(adminPassword);


        // setting auth header for customer:

        var loginRequest = new UserDto.Request.Login(
                userCustomer.getUsername(),
                userCustomer.getPassword());

        var loginResponse = restTemplate.postForEntity(
                apiURL + "/auth/signin",
                loginRequest,
                JwtAuthResponse.class);

        String authToken = loginResponse.getBody().getAccessToken();

        customerHeaders.setBearerAuth(authToken);


        // setting auth header for admin:

        var loginRequest1 = new UserDto.Request.Login(
                userAdmin.getUsername(),
                userAdmin.getPassword());

        var loginResponse1 = restTemplate.postForEntity(
                apiURL + "/auth/signin",
                loginRequest1,
                JwtAuthResponse.class);

        String authToken1 = loginResponse1.getBody().getAccessToken();

        adminHeaders.setBearerAuth(authToken1);
    }

    @Test
    void shouldCreateAndCompleteRentalSuccessfully () {
        // create
        Long stationId = 1L;
        var stationRequest = new StationDto.Request.Get(stationId, null);

        var createRentalResponse = restTemplate.exchange(
                apiURL + "/rentals",
                HttpMethod.POST,
                new HttpEntity<>(stationRequest, customerHeaders),
                RentalDto.Response.Basic.class
        );

        System.out.println(createRentalResponse);

        assertThat(createRentalResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI createdRentalLocation = createRentalResponse.getHeaders().getLocation();

        // start

        List<StationCommand> commandList = commandRepository
                .findByStationIdAndStatus(stationId, StationCommandStatus.PENDING);

        StationCommand command = commandList.getFirst();
        command.execute(rentalService);
        command.setStatus(StationCommandStatus.COMPLETED);
        commandRepository.save(command);

        var getRentalResponse = restTemplate.exchange(
                createdRentalLocation,
                HttpMethod.GET,
                new HttpEntity<>(customerHeaders),
                RentalDto.Response.Basic.class
        );

        System.out.println(getRentalResponse);

        // --

        Long rentalId = createRentalResponse.getBody().id();

        Rental rental = rentalRepository.findById(rentalId).get();
        rental.setStartTime(LocalDateTime.now().minusHours(4));
        rentalRepository.save(rental);

        // complete
        stationId = 2L;

        var completeRentalResponse = restTemplate.exchange(
                createdRentalLocation + "/complete",
                HttpMethod.PATCH,
                new HttpEntity<>(new StationDto.Request.Get(stationId, null), customerHeaders),
                RentalDto.Response.Basic.class
        );

        System.out.println(completeRentalResponse);

        // finish

        commandList = commandRepository
                .findByStationIdAndStatus(stationId, StationCommandStatus.PENDING);
        commandList.getFirst().execute(rentalService);


        getRentalResponse = restTemplate.exchange(
                createdRentalLocation,
                HttpMethod.GET,
                new HttpEntity<>(customerHeaders),
                RentalDto.Response.Basic.class
        );

        System.out.println(getRentalResponse);
    }

    @Test
    void shouldNotCreateRental() {
        var stationRequest = new StationDto.Request.Get(100L, null);

        var createRentalResponse = restTemplate.exchange(
                apiURL + "/rentals",
                HttpMethod.POST,
                new HttpEntity<>(stationRequest, customerHeaders),
                String.class
        );

        System.out.println(createRentalResponse);
        assertThat(createRentalResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getPagedRentals() {
        var getRentalsResponse = restTemplate.exchange(
                apiURL + "/rentals" + "?size=5&page=1",
                HttpMethod.GET,
                new HttpEntity<>(adminHeaders),
                PagedResponse.class
        );

        System.out.println(getRentalsResponse);
        assertThat(getRentalsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
