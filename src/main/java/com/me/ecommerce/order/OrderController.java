package com.me.ecommerce.order;

import com.me.ecommerce.gateway.model.PaymentResponse;
import com.me.ecommerce.order.message.OrderSummaryResponse;
import com.me.ecommerce.order.message.PayOrderRequest;
import com.me.ecommerce.order.message.ViewOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(
            value = "/api/order/pay",
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<String> payOrder(@RequestBody PayOrderRequest req) {
        PaymentResponse paymentResponse = orderService.payOrder(req);
        return new ResponseEntity<>(paymentResponse.getMessage(), paymentResponse.getStatus());
    }

    @GetMapping(value = "/api/order/summary")
    public ResponseEntity<OrderSummaryResponse> orderSummary(@RequestHeader("user-id") int userId, @RequestHeader("order-id") int orderId) {
        OrderSummaryResponse orderSummary = orderService.orderSummary(userId, orderId);
        return new ResponseEntity<>(orderSummary, HttpStatus.OK);
    }

}
