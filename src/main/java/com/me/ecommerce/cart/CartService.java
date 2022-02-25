package com.me.ecommerce.cart;

import com.me.ecommerce.cart.model.Cart;
import com.me.ecommerce.cart.model.CartItem;
import com.me.ecommerce.product.ProductRepository;
import com.me.ecommerce.product.model.Product;
import com.me.ecommerce.user.UserRepository;
import com.me.ecommerce.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public String addItemToCart(int userId, int itemId, int quantity) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Product productToAdd = productRepository.getById(itemId);

        Optional<Cart> isCartPresent = cartRepository.findByUserId(userId);

        if(isCartPresent.isEmpty()) {
            int cartId = this.createCartFromUserId(userId);
            Cart cart = cartRepository.getById(cartId);
            CartItem cartItem = new CartItem(cart, productToAdd, quantity, ts, ts);
            cartItemRepository.save(cartItem);
        } else {
            Cart cart = isCartPresent.get();
            CartItem cartItem = new CartItem(cart, productToAdd, quantity, ts, ts);
            cartItemRepository.save(cartItem);
        }

        // TODO: Better serialize output and include Error Handling
        return String.format("Successfully added %d quantity of productId: %d for user: %d", quantity, itemId, userId);
    }

    private int createCartFromUserId(int userId) {
        User user = userRepository.getById(userId);
        Cart newCart = new Cart(user, new Timestamp(System.currentTimeMillis()));
        return cartRepository.save(newCart).getId();
    }
}
