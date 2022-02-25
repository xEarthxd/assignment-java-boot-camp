package com.me.ecommerce.cart;

import com.me.ecommerce.cart.message.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping(
            value = "/api/cart/add-item",
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    public String addItemToCart(@RequestBody AddItemRequest req) {
        return cartService.addItemToCart(req.getUserId(), req.getProductId(), req.getQuantity());
    }

}
