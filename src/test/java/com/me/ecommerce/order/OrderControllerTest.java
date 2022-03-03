package com.me.ecommerce.order;

import com.me.ecommerce.order.message.OrderSummaryResponse;
import com.me.ecommerce.order.message.ViewOrderResponse;
import com.me.ecommerce.shared_components.model.ItemInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private OrderService orderService;

    @Test
    @DisplayName("Should return 200 and correct response data when call view order endpoint with existed order")
    void viewExistedOrderTest() {
        // Arrange
        ItemInfo mockInfo1 = new ItemInfo(100, "MockProduct1", 100.0f, "Mock Product For Test1");
        ItemInfo mockInfo2 = new ItemInfo(101, "MockProduct2", 20.0f, "Mock Product For Test2");
        ItemInfo mockInfo3 = new ItemInfo(102, "MockProduct3", 60.0f, "Mock Product For Test3");
        List<ItemInfo> mockItemsInfo = new ArrayList<>();
        mockItemsInfo.add(mockInfo1);
        mockItemsInfo.add(mockInfo2);
        mockItemsInfo.add(mockInfo3);
        ViewOrderResponse mockViewOrderRes = new ViewOrderResponse(100, 1, "IN_PROGRESS", 3, mockItemsInfo, 180.0f);
        when(orderService.viewOrder(100, 1)).thenReturn(Optional.of(mockViewOrderRes));

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-id", "100");
        headers.add("order-id", "1");

        // Act
        ResponseEntity<ViewOrderResponse> viewOrderResponse = testRestTemplate.exchange("/api/order/view-items", HttpMethod.GET, new HttpEntity<>(headers), ViewOrderResponse.class);

        // Assert
        assertEquals(HttpStatus.OK, viewOrderResponse.getStatusCode());
        assertEquals(3, viewOrderResponse.getBody().getItems().size());
        assertEquals(180.0f, viewOrderResponse.getBody().getAmount());
    }

    @Test
    @DisplayName("Should return 404 when call view order endpoint with non-existed order")
    void viewNonExistedOrderTest() {
        // Arrange
        when(orderService.viewOrder(100, 9999)).thenReturn(Optional.empty());

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-id", "100");
        headers.add("order-id", "9999");

        // Act
        ResponseEntity<ViewOrderResponse> viewOrderResponse = testRestTemplate.exchange("/api/order/view-items", HttpMethod.GET, new HttpEntity<>(headers), ViewOrderResponse.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, viewOrderResponse.getStatusCode());
    }

    @Test
    @DisplayName("Should return 200 with Order Summary when calling order summary endpoint")
    void orderSummaryTest() {
        // Arrange
        ItemInfo mockInfo1 = new ItemInfo(100, "MockProduct1", 100.0f, "Mock Product For Test1");
        ItemInfo mockInfo2 = new ItemInfo(101, "MockProduct2", 20.0f, "Mock Product For Test2");
        ItemInfo mockInfo3 = new ItemInfo(102, "MockProduct3", 60.0f, "Mock Product For Test3");
        List<ItemInfo> mockItemsInfo = new ArrayList<>();
        mockItemsInfo.add(mockInfo1);
        mockItemsInfo.add(mockInfo2);
        mockItemsInfo.add(mockInfo3);

        OrderSummaryResponse mockOrderSummary = new OrderSummaryResponse("INV-001", "John Doe", "99/99 Mock Address", "22/02/2022 14:14:14.00", 180.0f, mockItemsInfo.size(), mockItemsInfo);

        when(orderService.orderSummary(100, 1)).thenReturn(mockOrderSummary);

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-id", "100");
        headers.add("order-id", "1");

        ResponseEntity<OrderSummaryResponse> orderSummaryResponse = testRestTemplate.exchange("/api/order/summary", HttpMethod.GET, new HttpEntity<>(headers), OrderSummaryResponse.class);

        // Assert
        assertEquals(HttpStatus.OK, orderSummaryResponse.getStatusCode());
        assertEquals("INV-001", orderSummaryResponse.getBody().getInvoiceNo());
        assertEquals("John Doe", orderSummaryResponse.getBody().getName());
        assertEquals("99/99 Mock Address", orderSummaryResponse.getBody().getShippingAddress());
        assertEquals(180.0f, orderSummaryResponse.getBody().getAmount());
        assertEquals(3, orderSummaryResponse.getBody().getCount());
    }
}