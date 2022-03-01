package com.me.ecommerce.order;

import com.me.ecommerce.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByIdAndUserId(int orderId, int userId);
}
