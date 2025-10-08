package com.example.power_rent.repository;

import com.example.power_rent.enums.UserRole;
import com.example.power_rent.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldCreateAndFindUserSuccessfully() {
        User user1 = new User(
                "test@email.com",
                "firstUser",
                "password1",
                UserRole.CUSTOMER
        );
        userRepository.save(user1);
        Optional<User> user1_ = userRepository.findUserByEmail(user1.getEmail());
        assertThat(user1_).isPresent();

        Long userId = user1_.get().getId();
        assertThat(userRepository.findById(userId)).isPresent();
    }

}
