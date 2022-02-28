package com.me.ecommerce.order;

import com.me.ecommerce.cart.model.CartItem;
import com.me.ecommerce.order.model.Order;
import com.me.ecommerce.order.model.OrderItem;
import com.me.ecommerce.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

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
}
