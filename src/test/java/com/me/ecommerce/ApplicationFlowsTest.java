package com.me.ecommerce;

import com.me.ecommerce.cart.message.AddItemRequest;
import com.me.ecommerce.cart.message.CheckoutResponse;
import com.me.ecommerce.cart.message.ViewCartResponse;
import com.me.ecommerce.product.message.ProductResponse;
import com.me.ecommerce.product.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

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

        // Checkout cart
        ResponseEntity<CheckoutResponse> checkoutCart = testRestTemplate.exchange("/api/cart/checkout", HttpMethod.GET, new HttpEntity<>(headers), CheckoutResponse.class);
        assertEquals(1, checkoutCart.getBody().getUserId());
        assertEquals("Successfully checked out", checkoutCart.getBody().getMessage());

    }

}
