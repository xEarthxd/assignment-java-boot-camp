package com.me.ecommerce.cart;

import com.me.ecommerce.cart.model.Cart;
import com.me.ecommerce.user.UserRepository;
import com.me.ecommerce.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return an cart if user has created cart before")
    void findExistedCartByUserId() {
        // Arrange
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        User mockUser = new User(100, "John", "Doe", "Address Mock", ts, ts);
        userRepository.save(mockUser);

        Cart mockCart = new Cart(mockUser, ts);
        cartRepository.save(mockCart);

        // Act
        Optional<Cart> result = cartRepository.findByUserId(mockUser.getId());

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Should return no cart if user has never created cart before")
    void findNonExistedCartByUserId() {
        // Arrange
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        User mockUser = new User(100, "John", "Doe", "Address Mock", ts, ts);
        userRepository.save(mockUser);

        // Act
        Optional<Cart> result = cartRepository.findByUserId(mockUser.getId());

        // Assert
        assertTrue(result.isEmpty());
    }
}