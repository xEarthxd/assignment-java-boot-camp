package com.me.ecommerce.product;

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
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        /*
            Since we are using data.sql to pre-insert the data
            this test will also use the same datasource
            (which should not happen because test needs to be isolated)
            we will improve this when implementing Postgres for main app
        */

        // Act
        Product[] result = testRestTemplate.getForObject("/api/products", Product[].class);

        // Assert
        assertEquals(20, result.length);
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
        Optional<Product> result = productService.getById(100);

        // Assert
        assertTrue(result.isPresent());
    }
}