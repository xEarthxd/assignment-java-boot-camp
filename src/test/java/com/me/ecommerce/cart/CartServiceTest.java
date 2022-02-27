package com.me.ecommerce.cart;

import com.me.ecommerce.cart.message.ViewCartResponse;
import com.me.ecommerce.cart.model.Cart;
import com.me.ecommerce.cart.model.CartItem;
import com.me.ecommerce.product.ProductRepository;
import com.me.ecommerce.product.model.Product;
import com.me.ecommerce.user.UserRepository;
import com.me.ecommerce.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Test
    void addItemToCartWhenCartExisted() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        Product mockProduct1 = new Product(100, "MockProduct1", 100.0f, "Mock Product For Test", ts, ts);
        when(productRepository.getById(100)).thenReturn(mockProduct1);

        Product mockProduct2 = new Product(101, "MockProduct2", 100.0f, "Mock Product For Test", ts, ts);
        when(productRepository.getById(100)).thenReturn(mockProduct2);

        User mockUser = new User(100, "John", "Doe", "Address Mock", ts, ts);
        Cart mockCart = new Cart(mockUser, ts);
        when(cartRepository.findByUserId(100)).thenReturn(Optional.of(mockCart));

        cartService.addItemToCart(100, 100, 1);
        verify(cartItemRepository, times(1)).save(any());

        cartService.addItemToCart(100, 100, 10);
        verify(cartItemRepository, times(2)).save(any());
    }

    @Test
    void viewCart() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        User mockUser = new User(100, "John", "Doe", "Address Mock", ts, ts);
        Cart mockCart = new Cart(mockUser, ts);
        when(cartRepository.findByUserId(100)).thenReturn(Optional.of(mockCart));

        Product mockProduct1 = new Product(100, "MockProduct1", 100.0f, "Mock Product For Test", ts, ts);
        Product mockProduct2 = new Product(101, "MockProduct2", 100.0f, "Mock Product For Test", ts, ts);

        CartItem mockItem1 = new CartItem(mockCart, mockProduct1, 1, ts, ts);
        CartItem mockItem2 = new CartItem(mockCart, mockProduct2, 1, ts, ts);

        List<CartItem> mockItems = new ArrayList<>();
        mockItems.add(mockItem1);
        mockItems.add(mockItem2);

        mockCart.setCartItems(mockItems);

        ViewCartResponse response = cartService.viewCart(100);

        assertEquals(2, response.getCount());
    }
}