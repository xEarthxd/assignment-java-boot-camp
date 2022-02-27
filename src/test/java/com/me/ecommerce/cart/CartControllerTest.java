package com.me.ecommerce.cart;

import com.me.ecommerce.cart.message.AddItemRequest;
import com.me.ecommerce.cart.message.ItemInfo;
import com.me.ecommerce.cart.message.ViewCartResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CartControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private CartService cartService;

    @Test
    @DisplayName("Should return success message when add item to cart successfully")
    void addItemToCartTest() {
        // Arrange
        AddItemRequest reqBody = new AddItemRequest(100, 1, 10);
        String stubServiceReturn = String.format("Successfully added %d quantity of productId: %d for user: %d", reqBody.getQuantity(), reqBody.getProductId(), reqBody.getUserId());
        when(cartService.addItemToCart(100, 1, 10)).thenReturn(stubServiceReturn);

        // Act
        String response = testRestTemplate.postForObject("/api/cart/add-item", reqBody, String.class);

        // Assert
        assertEquals("Successfully added 10 quantity of productId: 1 for user: 100", response);
    }

    @Test
    @DisplayName("Should return 2 mock products when viewing cart item for mock user")
    void viewCartItemTest() {
        // Arrange
        ItemInfo mockInfo1 = new ItemInfo(100, "MockProduct1", 100.0f, "Mock Product For Test1");
        ItemInfo mockInfo2 = new ItemInfo(101, "MockProduct2", 20.0f, "Mock Product For Test2");
        List<ItemInfo> mockItemsInfo = new ArrayList<>();
        mockItemsInfo.add(mockInfo1);
        mockItemsInfo.add(mockInfo2);

        ViewCartResponse stubServiceReturn = new ViewCartResponse(100, 1, 2, mockItemsInfo);
        when(cartService.viewCart(100)).thenReturn(stubServiceReturn);

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-id", "100");


        // Act
        ResponseEntity<ViewCartResponse> response = testRestTemplate.exchange("/api/cart/view-items", HttpMethod.GET, new HttpEntity<>(headers), ViewCartResponse.class);
        ViewCartResponse cartResponse = response.getBody();

        // Assert
        assertEquals(2, cartResponse.getCount());
    }
}