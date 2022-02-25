package com.me.ecommerce.cart;

import com.me.ecommerce.cart.message.AddItemRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CartControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private CartService cartService;

    @Test
    void addItemToCart() {
        // Arrange
        AddItemRequest reqBody = new AddItemRequest(100, 1, 10);
        String stubServiceReturn = String.format("Successfully added %d quantity of productId: %d for user: %d", reqBody.getQuantity(), reqBody.getProductId(), reqBody.getUserId());
        when(cartService.addItemToCart(100, 1, 10)).thenReturn(stubServiceReturn);

        // Act
        String response = testRestTemplate.postForObject("/api/cart/add-item", reqBody, String.class);

        // Assert
        assertEquals("Successfully added 10 quantity of productId: 1 for user: 100", response);
    }
}