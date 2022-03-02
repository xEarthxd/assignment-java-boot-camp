package com.me.ecommerce.gateway;


import com.me.ecommerce.gateway.model.PaymentResponse;
import com.me.ecommerce.shared_components.model.CardInfo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentGateway {

    public PaymentGateway() {
    }

    public PaymentResponse payWithCard(CardInfo cardInfo, float amount) {
        // This is a stub for calling Payment API
        if (cardInfo.getCardNo().equals("5105105105105100") && cardInfo.getCvv().equals("849") && cardInfo.getExp().equals("23/10")) {
            return new PaymentResponse(HttpStatus.OK, String.format("Payment Success! name: %s amount: %.2f", cardInfo.getCardName(), amount));
        } else {
            return new PaymentResponse(HttpStatus.BAD_REQUEST, String.format("Payment Failed! name: %s amount: %.2f", cardInfo.getCardName(), amount));
        }
    }

}
