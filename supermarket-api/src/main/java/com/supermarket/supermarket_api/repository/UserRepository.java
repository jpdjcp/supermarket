package com.supermarket.supermarket_api.repository;

import com.supermarket.supermarket_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(
            value = "SELECT * FROM users WHERE last_login < :threshold",
            nativeQuery = true
    )
    List<User> findInactiveSince(@Param("threshold") Instant threshold);

    List<User> findByLastLoginBetween(Instant from, Instant to);
}
