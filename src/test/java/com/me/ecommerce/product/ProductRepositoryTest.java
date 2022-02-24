package com.me.ecommerce.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should return 3 products containing 'test' when running find by keyword 'test' (Regardless to case sensitive)")
    void findByNameWithPresentProduct() {
        // Arrange
        Product test1 = new Product(100, "Test1", 10.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        productRepository.save(test1);

        Product test2 = new Product(101, "test2", 20.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        productRepository.save(test2);

        Product test3 = new Product(102, "tEst3", 40.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        productRepository.save(test3);

        Product extra4 = new Product(103, "Extra", 100.0f, "Not containing keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        productRepository.save(extra4);

        // Act
        List<Product> result = productRepository.findByNameContainingIgnoreCase("test");

        // Assert
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Should return 0 elements when running find by keyword 'test'")
    void findByNameWithNotPresentProduct() {
        // Act
        List<Product> result = productRepository.findByNameContainingIgnoreCase("test");

        // Assert
        System.out.println(result.toString());
        assertTrue(result.isEmpty());
    }
}