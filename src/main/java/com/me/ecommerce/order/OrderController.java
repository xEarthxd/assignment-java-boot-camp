package com.me.ecommerce.order;

import com.me.ecommerce.order.message.ViewOrderResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/api/order/view-items")
    public ResponseEntity<ViewOrderResponse> viewOrder(@RequestHeader("user-id") int userId, @RequestHeader("order-id") int orderId) {
        Optional<ViewOrderResponse> orderResOpt = orderService.viewOrder(userId, orderId);
        if (orderResOpt.isPresent()) {
            ViewOrderResponse orderRes = orderResOpt.get();
            return new ResponseEntity<>(orderRes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
