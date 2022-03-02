package com.me.ecommerce.gateway;

import com.me.ecommerce.gateway.model.PaymentResponse;
import com.me.ecommerce.shared_components.model.CardInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentGatewayTest {

    @Autowired
    private PaymentGateway paymentGateway;

    @Test
    @DisplayName("Should return status 200 when pay with valid card")
    void payWithValidCard() {
//            "amount": 138.93
        CardInfo mockCardInfo = new CardInfo("John Doe", "5105105105105100", "849", "23/10");
        PaymentResponse response = paymentGateway.payWithCard(mockCardInfo, 138.93f);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Payment Success! name: John Doe amount: 138.93", response.getMessage());
    }

    @Test
    @DisplayName("Should return status 400 when pay with invalid card")
    void payWithInvalidCard() {
        CardInfo mockCardInfo = new CardInfo("John Doe", "999999999999999", "999", "10/10");
        PaymentResponse response = paymentGateway.payWithCard(mockCardInfo, 138.93f);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Payment Failed! name: John Doe amount: 138.93", response.getMessage());
    }
}