package com.me.ecommerce.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void shouldReturnTwentyElementsWhenRequestingForAllProducts() {
        // Arrange
        // Since we are using data.sql to pre-insert the data
        // this test will also use the same datasource
        // (which should not happen because test needs to be isolated)
        // we will improve this when implementing Postgres for main app

        // Act
        Product[] result = testRestTemplate.getForObject("/api/products", Product[].class);

        // Assert
        assertEquals(20, result.length);

    }
}