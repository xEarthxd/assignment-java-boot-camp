package com.me.ecommerce.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    void getAllByKeyword() {
        // Arrange
        List<Product> stubProducts = new ArrayList<>();
        stubProducts.add(new Product(100, "Test1", 10.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        stubProducts.add(new Product(101, "test2", 20.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        stubProducts.add(new Product(102, "tEst3", 40.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        when(productRepository.findByNameContainingIgnoreCase("test")).thenReturn(stubProducts);

        ProductService productService = new ProductService();
        productService.setRepository(productRepository);

        // Act
        List<Product> result = productService.getAllByKeyword("test");

        // Assert
        assertEquals(3, result.size());

    }
}