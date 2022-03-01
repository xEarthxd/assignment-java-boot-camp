package com.me.ecommerce.order;

import com.me.ecommerce.cart.model.Cart;
import com.me.ecommerce.cart.model.CartItem;
import com.me.ecommerce.order.message.ViewOrderResponse;
import com.me.ecommerce.order.model.Order;
import com.me.ecommerce.order.model.OrderItem;
import com.me.ecommerce.product.model.Product;
import com.me.ecommerce.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("Should called create order 1 time and insert order item 2 times when create order from cart with 2 items")
    void createOrderAndOrderItemTest() {
        // Arrange
        OrderService orderService = new OrderService();
        orderService.setOrderRepository(orderRepository);
        orderService.setOrderItemRepository(orderItemRepository);

        Timestamp ts = new Timestamp(System.currentTimeMillis());

        User mockUser = new User(100, "John", "Doe", "Address Mock", ts, ts);
        Cart mockCart = new Cart(mockUser, ts);
        Product mockProduct1 = new Product(100, "MockProduct1", 100.0f, "Mock Product For Test", ts, ts);
        Product mockProduct2 = new Product(101, "MockProduct2", 100.0f, "Mock Product For Test", ts, ts);

        CartItem mockItem1 = new CartItem(mockCart, mockProduct1, 1, ts, ts);
        CartItem mockItem2 = new CartItem(mockCart, mockProduct2, 1, ts, ts);
        List<CartItem> mockItems = new ArrayList<>();
        mockItems.add(mockItem1);
        mockItems.add(mockItem2);
        when(orderRepository.save(any())).thenReturn(new Order(mockUser, "IN_PROGRESS", ts, ts));
        orderService.createOrderAndOrderItem(mockUser, mockItems);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(2)).save(any(OrderItem.class));
    }

    @Test
    @DisplayName("Should generate ViewOrderResponse with 2 items and totalAmount=230.0 when running viewOrder method")
    void viewExistedOrderTest() {
        // Arrange
        OrderService orderService = new OrderService();
        orderService.setOrderRepository(orderRepository);
        orderService.setOrderItemRepository(orderItemRepository);

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        User mockUser = new User(100, "John", "Doe", "Address Mock", ts, ts);
        Product mockProduct1 = new Product(100, "MockProduct1", 100.0f, "Mock Product For Test", ts, ts);
        Product mockProduct2 = new Product(101, "MockProduct2", 130.0f, "Mock Product For Test", ts, ts);

        Order mockOrder = new Order(mockUser, "IN_PROGRESS", ts, ts);
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem1 = new OrderItem(mockOrder, mockProduct1, 1, ts, ts);
        orderItems.add(orderItem1);
        OrderItem orderItem2 = new OrderItem(mockOrder, mockProduct2, 4, ts, ts);
        orderItems.add(orderItem2);
        mockOrder.setOrderItems(orderItems);

        when(orderRepository.findByIdAndUserId(0, 100)).thenReturn(Optional.of(mockOrder));

        // Act
        ViewOrderResponse viewOrder = orderService.viewOrder(100, 0).get();

        // Assert
        assertEquals(2, viewOrder.getItems().size());
        assertEquals(230.0f, viewOrder.getAmount());
    }

    @Test
    @DisplayName("Should return null when running viewOrder on non-existed order")
    void viewNonExistedOrderTest() {
        // Arrange
        OrderService orderService = new OrderService();
        orderService.setOrderRepository(orderRepository);
        orderService.setOrderItemRepository(orderItemRepository);

        when(orderRepository.findByIdAndUserId(0, 100)).thenReturn(Optional.empty());

        // Act
        Optional<ViewOrderResponse> viewOrder = orderService.viewOrder(100, 0);

        // Assert
        assertTrue(viewOrder.isEmpty());
    }
}