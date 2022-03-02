package com.me.ecommerce.order;

import com.me.ecommerce.cart.model.Cart;
import com.me.ecommerce.cart.model.CartItem;
import com.me.ecommerce.gateway.PaymentGateway;
import com.me.ecommerce.gateway.model.PaymentResponse;
import com.me.ecommerce.order.message.PayOrderRequest;
import com.me.ecommerce.order.message.ViewOrderResponse;
import com.me.ecommerce.order.model.Order;
import com.me.ecommerce.order.model.OrderItem;
import com.me.ecommerce.product.model.Product;
import com.me.ecommerce.shared_components.model.CardInfo;
import com.me.ecommerce.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private PaymentGateway paymentGateway;

    @Test
    @DisplayName("Should called create order 1 time and insert order item 2 times when create order from cart with 2 items")
    void createOrderAndOrderItemTest() {
        // Arrange
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
        when(orderRepository.findByIdAndUserId(0, 100)).thenReturn(Optional.empty());

        // Act
        Optional<ViewOrderResponse> viewOrder = orderService.viewOrder(100, 0);

        // Assert
        assertTrue(viewOrder.isEmpty());
    }


    @Test
    @DisplayName("Should return 200 status if calling to payment gateway success")
    void payWithValidCard() {
        // Arrange
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        User mockUser = new User(100, "John", "Doe", "Address Mock", ts, ts);
        Product mockProduct1 = new Product(100, "MockProduct1", 100.0f, "Mock Product For Test", ts, ts);
        Product mockProduct2 = new Product(101, "MockProduct2", 130.0f, "Mock Product For Test", ts, ts);

        Order mockOrder = new Order(mockUser, "IN_PROGRESS", ts, ts);

        when(orderRepository.findByIdAndUserId(0, 100)).thenReturn(Optional.of(mockOrder));

        CardInfo mockCardInfo = new CardInfo("John Doe", "5105105105105100", "849", "23/10");
        PayOrderRequest mockReq = new PayOrderRequest(100, 0, "CARD", mockCardInfo, 230.0f);

        PaymentResponse mockPaymentResponse = new PaymentResponse(HttpStatus.OK, String.format("Payment Success! name: %s amount: 230.00", mockCardInfo.getCardName()));
        when(paymentGateway.payWithCard(mockCardInfo, 230.0f)).thenReturn(mockPaymentResponse);

        // Act
        PaymentResponse paymentResponse = orderService.payOrder(mockReq);

        // Assert
        assertEquals(HttpStatus.OK, paymentResponse.getStatus());
        assertEquals("Payment Success! name: John Doe amount: 230.00", paymentResponse.getMessage());
    }

    @Test
    @DisplayName("Should return 400 if calling to payment gateway failed with invalid card no")
    void payWithInvalidCard() {
        // Arrange
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        User mockUser = new User(100, "John", "Doe", "Address Mock", ts, ts);
        Product mockProduct1 = new Product(100, "MockProduct1", 100.0f, "Mock Product For Test", ts, ts);
        Product mockProduct2 = new Product(101, "MockProduct2", 130.0f, "Mock Product For Test", ts, ts);

        Order mockOrder = new Order(mockUser, "IN_PROGRESS", ts, ts);

        when(orderRepository.findByIdAndUserId(0, 100)).thenReturn(Optional.of(mockOrder));

        CardInfo mockCardInfo = new CardInfo("John Doe", "9999999999", "999", "99/99");
        PayOrderRequest mockReq = new PayOrderRequest(100, 0, "CARD", mockCardInfo, 230.0f);

        PaymentResponse mockPaymentResponse = new PaymentResponse(HttpStatus.BAD_REQUEST, String.format("Payment Failure! name: %s amount: 230.00", mockCardInfo.getCardName()));
        when(paymentGateway.payWithCard(mockCardInfo, 230.0f)).thenReturn(mockPaymentResponse);

        // Act
        PaymentResponse paymentResponse = orderService.payOrder(mockReq);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, paymentResponse.getStatus());
        assertEquals("Payment Failure! name: John Doe amount: 230.00", paymentResponse.getMessage());
    }

    @Test
    @DisplayName("Should return 400 if payment channel is not supported")
    void payWithNotSupportedPaymentChannel() {
        // Arrange
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        User mockUser = new User(100, "John", "Doe", "Address Mock", ts, ts);
        Product mockProduct1 = new Product(100, "MockProduct1", 100.0f, "Mock Product For Test", ts, ts);
        Product mockProduct2 = new Product(101, "MockProduct2", 130.0f, "Mock Product For Test", ts, ts);

        Order mockOrder = new Order(mockUser, "IN_PROGRESS", ts, ts);

        when(orderRepository.findByIdAndUserId(0, 100)).thenReturn(Optional.of(mockOrder));

        PayOrderRequest mockReq = new PayOrderRequest(100, 0, "LINE_PAY", null, 230.0f);

        // Act
        PaymentResponse paymentResponse = orderService.payOrder(mockReq);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, paymentResponse.getStatus());
        assertEquals("PaymentChannel not supported", paymentResponse.getMessage());
    }

    @Test
    @DisplayName("Should return 404 if order cannot be found")
    void payNonExistedOrder() {
        // Arrange
        when(orderRepository.findByIdAndUserId(0, 100)).thenReturn(Optional.empty());

        CardInfo mockCardInfo = new CardInfo("John Doe", "5105105105105100", "849", "23/10");
        PayOrderRequest mockReq = new PayOrderRequest(100, 0, "CARD", mockCardInfo, 230.0f);

        // Act
        PaymentResponse paymentResponse = orderService.payOrder(mockReq);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, paymentResponse.getStatus());
        assertEquals("Order cannot be found for userId: 100", paymentResponse.getMessage());
    }
}