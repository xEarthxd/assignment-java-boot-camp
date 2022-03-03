package com.me.ecommerce.order;

import com.me.ecommerce.cart.model.CartItem;
import com.me.ecommerce.gateway.PaymentGateway;
import com.me.ecommerce.gateway.model.PaymentResponse;
import com.me.ecommerce.order.message.OrderSummaryResponse;
import com.me.ecommerce.order.message.PayOrderRequest;
import com.me.ecommerce.order.message.ViewOrderResponse;
import com.me.ecommerce.order.model.Order;
import com.me.ecommerce.order.model.OrderItem;
import com.me.ecommerce.user.UserRepository;
import com.me.ecommerce.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentGateway paymentGateway;

    @Autowired
    private UserRepository userRepository;

    public OrderService() {
    }

    public PaymentGateway getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderItemRepository getOrderItemRepository() {
        return orderItemRepository;
    }

    public void setOrderItemRepository(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public int createOrderAndOrderItem(User user, List<CartItem> cartItems) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Order order = new Order(user, "IN_PROGRESS", ts, ts);
        Order savedOrder = orderRepository.save(order);
        orderRepository.flush();
        for(CartItem cartItem : cartItems) {
            orderItemRepository.save(new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity(), ts, ts));
        }
        return savedOrder.getId();
    }

    public Optional<ViewOrderResponse> viewOrder(int userId, int orderId) {
        // change get() to condition
        Optional<Order> orderOpt = orderRepository.findByIdAndUserId(orderId, userId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            return Optional.of(new ViewOrderResponse(userId, orderId, order.getStatus(), order.getOrderItems().size(), order.getItemInfo(), order.getTotalAmount()));
        } else {
            return Optional.empty();
        }
    }

    public PaymentResponse payOrder(PayOrderRequest request) {
        Optional<Order> order = orderRepository.findByIdAndUserId(request.getOrderId(), request.getUserId());
        if(order.isEmpty()) {
            return new PaymentResponse(HttpStatus.NOT_FOUND, "Order cannot be found for userId: "+request.getUserId());
        }

        if(order.get().getTotalAmount() != request.getAmount()) {
            return new PaymentResponse(HttpStatus.BAD_REQUEST, String.format("Invalid payment amount: %.2f [Should be: %.2f]", request.getAmount(), order.get().getTotalAmount()));
        }

        PaymentResponse paymentResponse;
        switch (request.getPaymentChannel()) {
            case "CARD":
                paymentResponse = paymentGateway.payWithCard(request.getCardInfo(), request.getAmount());
                if(paymentResponse.getStatus() == HttpStatus.OK) {
                    Order userOrder = order.get();
                    userOrder.setStatus("PAID");
                    userOrder.setModifiedAt(new Timestamp(System.currentTimeMillis()));
                    orderRepository.save(userOrder);
                }
                break;
            default:
                paymentResponse = new PaymentResponse(HttpStatus.BAD_REQUEST, "PaymentChannel not supported");
        }
        return paymentResponse;
    }

    public OrderSummaryResponse orderSummary(int userId, int orderId) {
        User user = userRepository.getById(userId);
        Order order = orderRepository.findByIdAndUserId(orderId, userId).get();

        return new OrderSummaryResponse(
                invoiceGenerator(orderId),
                user.getFullName(),
                user.getAddress(),
                order.getModifiedAt().toString(),
                order.getTotalAmount(),
                order.getOrderItems().size(),
                order.getItemInfo()
        );
    }

    private String invoiceGenerator(int orderId) {
        return String.format("INV-%03d", orderId);
    }

}
