package com.me.ecommerce.product;

import com.me.ecommerce.product.message.ProductResponse;
import com.me.ecommerce.product.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private ProductService productService;

    @Test
    void shouldReturnTwentyElementsWhenRequestingForAllProducts() {

        // Arrange
        List<ProductResponse> stubProducts = new ArrayList<>();
        stubProducts.add(new ProductResponse(100, "Test1", 10.0f, "Mock Product .."));
        stubProducts.add(new ProductResponse(101, "test2", 20.0f, "Mock Product .."));
        stubProducts.add(new ProductResponse(102, "tEst3", 40.0f, "Mock Product .."));
        stubProducts.add(new ProductResponse(103, "ProductX", 40.0f, "Mock Product .."));
        stubProducts.add(new ProductResponse(104, "ProductY", 40.0f, "Mock Product .."));
        when(productService.getAllProduct()).thenReturn(stubProducts);

        // Act
        ProductResponse[] result = testRestTemplate.getForObject("/api/products", ProductResponse[].class);

        // Assert
        assertEquals(5, result.length);
    }

    @Test
    void shouldReturnProductsContainingKeywordRequestedWithKeywordQueryString() {
        // Arrange
        List<Product> stubProducts = new ArrayList<>();
        stubProducts.add(new Product(100, "Test1", 10.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        stubProducts.add(new Product(101, "test2", 20.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        stubProducts.add(new Product(102, "tEst3", 40.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        when(productService.getAllByKeyword("test")).thenReturn(stubProducts);

        // Act
        Product[] result = testRestTemplate.getForObject("/api/products?keyword=test", Product[].class);

        // Assert
        assertEquals(3, result.length);
    }

    @Test
    void shouldReturnProductWhenRequestedWithProductId() {
        // Arrange
        Product product = new Product(100, "Test1", 10.0f, "Contain keyword", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        when(productService.getById(100)).thenReturn(Optional.of(product));

        // Act
        Product result = testRestTemplate.getForObject("/api/products/100", Product.class);

        // Assert
        assertEquals("Test1", result.getName());
    }
}