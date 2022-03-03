package com.me.ecommerce;

import com.me.ecommerce.cart.message.AddItemRequest;
import com.me.ecommerce.cart.message.CheckoutResponse;
import com.me.ecommerce.cart.message.ViewCartResponse;
import com.me.ecommerce.order.message.OrderSummaryResponse;
import com.me.ecommerce.order.message.PayOrderRequest;
import com.me.ecommerce.order.message.ViewOrderResponse;
import com.me.ecommerce.product.message.ProductResponse;
import com.me.ecommerce.product.model.Product;
import com.me.ecommerce.shared_components.model.CardInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationFlowsTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void applicationFlowTest() {
        // ------------------
        // Get all products
        ProductResponse[] getAllProductRes = testRestTemplate.getForObject("/api/products", ProductResponse[].class);
        assertEquals(20, getAllProductRes.length);

        // ------------------
        // Get products by keyword "men"
        ProductResponse[] productByKeywordRes = testRestTemplate.getForObject("/api/products?keyword=men", ProductResponse[].class);
        assertEquals(11, productByKeywordRes.length);

        // ------------------
        // Choose product by productId
        Product productId10 = testRestTemplate.getForObject("/api/products/10", Product.class);
        assertTrue(productId10.getName().contains("SanDisk SSD"));

        // ------------------
        // Add item to cart 1st time
        AddItemRequest reqBody = new AddItemRequest(1, 10, 1);
        testRestTemplate.postForObject("/api/cart/add-item", reqBody, String.class);

        // View Cart item -> Should contain 1 item
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-id", "1");

        ResponseEntity<ViewCartResponse> viewCartRes1 = testRestTemplate.exchange("/api/cart/view-items", HttpMethod.GET, new HttpEntity<>(headers), ViewCartResponse.class);
        ViewCartResponse cartResponse1 = viewCartRes1.getBody();

        assertEquals(1, cartResponse1.getCount());

        // ------------------
        // Add item to cart 2nd time
        AddItemRequest reqBody2 = new AddItemRequest(1, 11, 1);
        testRestTemplate.postForObject("/api/cart/add-item", reqBody2, String.class);

        // View Cart item -> Should contain 2 items
        ResponseEntity<ViewCartResponse> viewCartRes2 = testRestTemplate.exchange("/api/cart/view-items", HttpMethod.GET, new HttpEntity<>(headers), ViewCartResponse.class);
        ViewCartResponse cartResponse2 = viewCartRes2.getBody();

        assertEquals(2, cartResponse2.getCount());

        // ------------------
        // Checkout cart
        ResponseEntity<CheckoutResponse> checkoutCart = testRestTemplate.exchange("/api/cart/checkout", HttpMethod.GET, new HttpEntity<>(headers), CheckoutResponse.class);
        assertEquals(1, checkoutCart.getBody().getUserId());
        assertEquals("Successfully checked out", checkoutCart.getBody().getMessage());

        // ------------------
        // View orders
        HttpHeaders headersUserAndOrderId = new HttpHeaders();
        headersUserAndOrderId.add("user-id", "1");
        headersUserAndOrderId.add("order-id", "1");

        ResponseEntity<ViewOrderResponse> viewOrderResponse = testRestTemplate.exchange("/api/order/view-items", HttpMethod.GET, new HttpEntity<>(headersUserAndOrderId), ViewOrderResponse.class);

        assertEquals(HttpStatus.OK, viewOrderResponse.getStatusCode());
        assertEquals(2, viewOrderResponse.getBody().getItems().size());
        assertEquals(218.0f, viewOrderResponse.getBody().getAmount());
        assertEquals("IN_PROGRESS", viewOrderResponse.getBody().getOrderStatus());

        // ------------------
        // Pay for order
        CardInfo mockCardInfo = new CardInfo("John Doe", "5105105105105100", "849", "23/10");
        PayOrderRequest mockReqBody = new PayOrderRequest(1, 1, "CARD", mockCardInfo, 218.0f);

        ResponseEntity<String> payResponse = testRestTemplate.postForEntity( "/api/order/pay", mockReqBody, String.class);

        assertEquals(HttpStatus.OK, payResponse.getStatusCode());
        ResponseEntity<ViewOrderResponse> viewPaidOrder = testRestTemplate.exchange("/api/order/view-items", HttpMethod.GET, new HttpEntity<>(headersUserAndOrderId), ViewOrderResponse.class);
        assertEquals("PAID", viewPaidOrder.getBody().getOrderStatus());

        // Order Summary
        ResponseEntity<OrderSummaryResponse> orderSummaryResponse = testRestTemplate.exchange("/api/order/summary", HttpMethod.GET, new HttpEntity<>(headersUserAndOrderId), OrderSummaryResponse.class);

        assertEquals(HttpStatus.OK, orderSummaryResponse.getStatusCode());
        assertEquals("INV-001", orderSummaryResponse.getBody().getInvoiceNo());
        assertEquals("John Mayer", orderSummaryResponse.getBody().getName());
        assertEquals("142/34-5  Bang Phueng, Pra Pradaeng, Samutprakarn, Thailand 10130", orderSummaryResponse.getBody().getShippingAddress());
        assertEquals(218.0f, orderSummaryResponse.getBody().getAmount());
        assertEquals(2, orderSummaryResponse.getBody().getCount());
    }

}
