package com.me.ecommerce.order;

import com.me.ecommerce.cart.model.CartItem;
import com.me.ecommerce.order.message.ViewOrderResponse;
import com.me.ecommerce.order.model.Order;
import com.me.ecommerce.order.model.OrderItem;
import com.me.ecommerce.product.model.Product;
import com.me.ecommerce.shared_components.model.ItemInfo;
import com.me.ecommerce.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    public OrderService() {
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
}
