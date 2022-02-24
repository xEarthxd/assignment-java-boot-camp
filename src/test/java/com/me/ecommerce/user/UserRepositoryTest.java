package com.me.ecommerce.user;

import com.me.ecommerce.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should return present user when find existed user by id")
    void findExistingUserById() {
        // Arrange
        User user = new User(100, "John", "Doe", "Mock Address 1", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findById(100);

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Should return null when user is not existed")
    void findNonExistingUserById() {
        // Act
        Optional<User> result = userRepository.findById(100);

        // Assert
        assertTrue(result.isEmpty());
    }
}