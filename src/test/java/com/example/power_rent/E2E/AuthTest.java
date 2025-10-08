package com.example.power_rent.E2E;

import com.example.power_rent.model.Station;
import com.example.power_rent.security.JwtAuthResponse;
import com.example.power_rent.dto.UserDto;
import com.example.power_rent.enums.UserRole;
import com.example.power_rent.model.User;
import com.example.power_rent.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URI;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTest {

    @LocalServerPort
    private int port;
    private String apiURL;

    private User userCustomer, userAdmin;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void beforeEach() {
        apiURL = "http://localhost:" + port;

        userRepository.deleteAll();

        userCustomer = new User(
                "customer@email.com",
                "customer",
                "customerPassword",
                UserRole.CUSTOMER
        );
        userRepository.save(new User(
                userCustomer.getEmail(),
                userCustomer.getUsername(),
                passwordEncoder.encode(userCustomer.getPassword()),
                userCustomer.getRole()
        ));

        userAdmin = new User(
                "admin@email.com",
                "admin",
                "adminPassword",
                UserRole.ADMIN
        );
        userRepository.save(new User(
                userAdmin.getEmail(),
                userAdmin.getUsername(),
                passwordEncoder.encode(userAdmin.getPassword()),
                userAdmin.getRole()
        ));
    }


    @Test
    void shouldSignupSuccessfully() {
        UserDto.Request.SignUp signUpRequest = new UserDto.Request.SignUp(
                "test@email.com",
                "somepassword",
                null);

        ResponseEntity<UserDto.Response.Basic> postResponse = restTemplate.postForEntity(
                apiURL + "/auth/signup",
                signUpRequest,
                UserDto.Response.Basic.class);

        System.out.println("signup response: \n\t" + postResponse);
        Long userId = postResponse.getBody().id();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(userId).isNotNull();

    }

    @Test
    void shouldNotSignupWithExistingEmail() {
        UserDto.Request.SignUp signUpRequest = new UserDto.Request.SignUp(
                userCustomer.getEmail(),
                "somepassword",
                null);

        var postResponse = restTemplate.postForEntity(
                apiURL + "/auth/signup",
                signUpRequest,
                String.class);

        System.out.println("signup response: \n\t" + postResponse);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotSignupWithUnvalidFormat() {
        UserDto.Request.SignUp signUpRequest1 = new UserDto.Request.SignUp(
                "test",
                "somepassword",
                null);

        var postResponse1 = restTemplate.postForEntity(
                apiURL + "/auth/signup",
                signUpRequest1,
                String.class);

        System.out.println("signup response (incorrect email format): \n\t" + postResponse1);
        assertThat(postResponse1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);


        UserDto.Request.SignUp signUpRequest2 = new UserDto.Request.SignUp(
                "test@email.com",
                "",
                null);

        var postResponse2 = restTemplate.postForEntity(
                apiURL + "/auth/signup",
                signUpRequest2,
                String.class);

        System.out.println("signup response (empty password): \n\t" + postResponse2);
        assertThat(postResponse2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldLoginSuccessfully() {
        UserDto.Request.Login loginRequest = new UserDto.Request.Login(
                userCustomer.getUsername(),
                userCustomer.getPassword());

        ResponseEntity<JwtAuthResponse> loginResponse = restTemplate.postForEntity(
                apiURL + "/auth/signin",
                loginRequest,
                JwtAuthResponse.class);

        System.out.println("login response: \n\t" + loginResponse);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody().getAccessToken()).isNotNull();
    }

    @Test
    void shouldAccessWithToken() {
        // receiving a token
        UserDto.Request.Login loginRequest = new UserDto.Request.Login(
                userCustomer.getUsername(),
                userCustomer.getPassword());

        ResponseEntity<JwtAuthResponse> loginResponse = restTemplate.postForEntity(
                apiURL + "/auth/signin",
                loginRequest,
                JwtAuthResponse.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        String authToken = loginResponse.getBody().getAccessToken();

        // trying to access endpoint "/api/users/me"
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        ResponseEntity<UserDto.Response.Basic> getUserInfoResponse = restTemplate.exchange(
                apiURL + "/users/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserDto.Response.Basic.class);

        System.out.println("access response: \n\t" + getUserInfoResponse);

        assertThat(getUserInfoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUserInfoResponse.getBody().email()).isEqualTo(userCustomer.getEmail());
    }

    @Test
    void shouldNotAccessUnauthorized() {
        var getUserInfoResponse = restTemplate.getForEntity(
                apiURL + "/users/me",
                String.class);

        System.out.println("access response: \n\t" + getUserInfoResponse);

        assertThat(getUserInfoResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // TODO: wrong exception handling
    @Test
    void shouldNotLoginWithWrongUsernameOrPassword() {
        UserDto.Request.Login loginRequest1 = new UserDto.Request.Login(
                userCustomer.getEmail(),
                userCustomer.getPassword() + "e");

        var loginResponse1 = restTemplate.postForEntity(
                apiURL + "/auth/signin",
                loginRequest1,
                String.class);

        System.out.println("login response: \n\t" + loginResponse1);
        assertThat(loginResponse1.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);


        UserDto.Request.Login loginRequest2 = new UserDto.Request.Login(
                userCustomer.getEmail(),
                "");

        var loginResponse2 = restTemplate.postForEntity(
                apiURL + "/auth/signin",
                loginRequest2,
                String.class);

        System.out.println("login response: \n\t" + loginResponse2);
        assertThat(loginResponse2.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


//    @Test
    void allInOne() {
        UserDto.Request.SignUp signUpRequest = new UserDto.Request.SignUp(
                "test@email.com",
                "somepassword",
                null);

        ResponseEntity<UserDto.Response.Basic> postResponse = restTemplate.postForEntity(
                apiURL + "/auth/signup",
                signUpRequest,
                UserDto.Response.Basic.class);

        Long userId = postResponse.getBody().id();
        assertThat(userId).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        System.out.println("signup response: \n\t" + postResponse);


        // get user test
        URI location = postResponse.getHeaders().getLocation();
        ResponseEntity<UserDto.Response.Basic> getResponse = restTemplate.getForEntity(
                location,
                UserDto.Response.Basic.class);

        System.out.println("get user response: \n\t" + getResponse);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);


        // login test
        UserDto.Request.Login loginRequest = new UserDto.Request.Login(
                signUpRequest.email(),
                signUpRequest.password());

        ResponseEntity<JwtAuthResponse> loginResponse = restTemplate.postForEntity(
                apiURL + "/auth/signin",
                loginRequest,
                JwtAuthResponse.class);

        System.out.println("login response: \n\t" + loginResponse);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody().getAccessToken()).isNotNull();

        // access tests
        ResponseEntity<UserDto.Response.Basic> getUserInfoResponse = restTemplate.getForEntity(
                apiURL + "/users/me",
                UserDto.Response.Basic.class);

        System.out.println("1st access response: \n\t" + getUserInfoResponse);

        assertThat(getUserInfoResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        String authToken = loginResponse.getBody().getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        ResponseEntity<UserDto.Response.Basic> getUserInfoResponse2 = restTemplate.exchange(
                apiURL + "/users/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserDto.Response.Basic.class);

        System.out.println("2nd access response: \n\t" + getUserInfoResponse2);

        assertThat(getUserInfoResponse2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUserInfoResponse2.getBody().id()).isEqualTo(userId);
    }
}
