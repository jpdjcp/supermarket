package com.supermarket.supermarket_api.integration;

import com.supermarket.supermarket_api.model.User;
import com.supermarket.supermarket_api.model.UserRole;
import com.supermarket.supermarket_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private String username;
    private String password;
    private UserRole role;
    private User user;

    @BeforeEach
    void setup() {
        username = "John Jackson";
        password = "h1cs65dd54";
        role = UserRole.ROLE_USER;
        user = new User(username, password, role);
    }

    @Test
    void shouldSaveAndRetrieveUser() {
        user = userRepository.save(user);

        assertThat(user.getId()).isNotNull();

        Optional<User> found = userRepository.findById(user.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo(username);
        assertThat(found.get().getPassword()).isEqualTo(password);
        assertThat(found.get().getRole()).isEqualTo(role);
        assertThat(found.get().getCreatedAt()).isNotNull();
        assertThat(found.get().isEnabled()).isTrue();
        assertThat(found.get().getLastLogin()).isNull();
    }

    @Test
    void shouldFindInactiveSince() {
        user.setLastLogin(Instant.now().minusSeconds(10L));
        userRepository.save(user);
        userRepository.flush();
        List<User> inactives = userRepository
                .findInactiveSince(Instant.now());

        assertThat(inactives).hasSize(1);
    }
}
