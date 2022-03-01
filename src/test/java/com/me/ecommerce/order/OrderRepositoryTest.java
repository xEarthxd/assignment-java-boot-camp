package com.me.ecommerce.order;

import com.me.ecommerce.order.model.Order;
import com.me.ecommerce.order.model.OrderItem;
import com.me.ecommerce.product.ProductRepository;
import com.me.ecommerce.product.model.Product;
import com.me.ecommerce.user.UserRepository;
import com.me.ecommerce.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should return 2 items with totalAmount 30 when find order by order-id / user-id")
    void findByIdAndUserId() {
        // Arrange
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        User mockUser = new User(100, "John", "Doe", "Address Mock", ts, ts);
        userRepository.save(mockUser);

        Product mockProduct1 = new Product(100, "Test1", 10.0f, "Contain keyword", ts, ts);
        productRepository.save(mockProduct1);
        Product mockProduct2 = new Product(101, "test2", 20.0f, "Contain keyword", ts, ts);
        productRepository.save(mockProduct2);
        Order mockOrder = new Order(mockUser, "IN_PROGRESS", ts, ts);
        List<OrderItem> mockOrderItems = new ArrayList<>();
        OrderItem orderItem1 = new OrderItem(mockOrder, mockProduct1, 1, ts, ts);
        mockOrderItems.add(orderItem1);
        OrderItem orderItem2 = new OrderItem(mockOrder, mockProduct2, 4, ts, ts);
        mockOrderItems.add(orderItem2);
        mockOrder.setOrderItems(mockOrderItems);
        orderRepository.save(mockOrder);


        Order orderResult = orderRepository.findByIdAndUserId(1, 100).get();

        assertEquals(2, orderResult.getOrderItems().size());
        assertEquals(30.0f, orderResult.getTotalAmount());

    }
}