package com.me.ecommerce.product;

import com.me.ecommerce.product.message.ProductResponse;
import com.me.ecommerce.product.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should return 5 products from repository when calling getAllProduct")
    void getAllProductsTest() {
        // Arrange
        List<Product> stubProducts = new ArrayList<>();
        stubProducts.add(new Product(100, "Test1", 10.0f, "Mock Product ..", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        stubProducts.add(new Product(101, "test2", 20.0f, "Mock Product ..", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        stubProducts.add(new Product(102, "tEst3", 40.0f, "Mock Product ..", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        stubProducts.add(new Product(103, "ProductX", 40.0f, "Mock Product ..", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        stubProducts.add(new Product(104, "ProductY", 40.0f, "Mock Product ..", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        when(productRepository.findAll()).thenReturn(stubProducts);

        ProductService productService = new ProductService();
        productService.setRepository(productRepository);

        // Act
        List<ProductResponse> result = productService.getAllProduct();

        // Assert
        assertEquals(5, result.size());
    }

    @Test
    @DisplayName("Should return 3 products containing 'test' keyword from repository regardless case sensitivity")
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

    @Test
    @DisplayName("Should return an existing product when searching when product id")
    void getExistingProductById() {
        // Arrange
        Product stubProduct = new Product(100, "Test1", 10.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        when(productRepository.findById(100)).thenReturn(Optional.of(stubProduct));

        ProductService productService = new ProductService();
        productService.setRepository(productRepository);

        // Act
        Optional<Product> result = productService.getById(100);

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Should return null when when searching when non-existing product id")
    void getNonExistingProductById() {
        // Arrange
        when(productRepository.findById(100)).thenReturn(Optional.empty());

        ProductService productService = new ProductService();
        productService.setRepository(productRepository);

        // Act
        Optional<Product> result = productService.getById(100);

        // Assert
        assertTrue(result.isEmpty());
    }
}