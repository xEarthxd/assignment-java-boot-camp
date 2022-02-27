package com.me.ecommerce.cart;

import com.me.ecommerce.cart.message.AddItemRequest;
import com.me.ecommerce.cart.message.ViewCartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/api/cart/view-items")
    public ResponseEntity<ViewCartResponse> viewCart(@RequestHeader("user-id") int userId) {
        return new ResponseEntity<ViewCartResponse>(cartService.viewCart(userId), HttpStatus.OK);
    }

}
