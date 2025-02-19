package com.me.ecommerce.cart;

import com.me.ecommerce.cart.message.CheckoutResponse;
import com.me.ecommerce.shared_components.model.ItemInfo;
import com.me.ecommerce.cart.message.ViewCartResponse;
import com.me.ecommerce.cart.model.Cart;
import com.me.ecommerce.cart.model.CartItem;
import com.me.ecommerce.order.OrderService;
import com.me.ecommerce.product.ProductRepository;
import com.me.ecommerce.product.model.Product;
import com.me.ecommerce.user.UserRepository;
import com.me.ecommerce.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private OrderService orderService;

    public String addItemToCart(int userId, int itemId, int quantity) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Product productToAdd = productRepository.getById(itemId);

        Optional<Cart> userCart = cartRepository.findByUserId(userId);

        if(userCart.isPresent()) {
            Cart cart = userCart.get();
            CartItem cartItem = new CartItem(cart, productToAdd, quantity, ts, ts);
            cartItemRepository.save(cartItem);
        } else {
            int cartId = this.createCartFromUserId(userId);
            Cart cart = cartRepository.getById(cartId);
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

    public ViewCartResponse viewCart(int userId) {
        Optional<Cart> userCart = cartRepository.findByUserId(userId);
        ViewCartResponse cartResult;
        if (userCart.isPresent()) {
            Cart cart = userCart.get();
            List<ItemInfo> cartItemsInfo = new ArrayList<ItemInfo>();
            for (CartItem item : cart.getCartItems()) {
                Product product = item.getProduct();
                ItemInfo itemInfo = new ItemInfo(product.getId(), product.getName(), product.getPrice(), product.getDescription());
                cartItemsInfo.add(itemInfo);
            }
            cartResult = new ViewCartResponse(userId, cart.getId(), cartItemsInfo.size(), cartItemsInfo);
        } else {
            int cartId = this.createCartFromUserId(userId);
            cartResult = new ViewCartResponse(userId, cartId, 0, new ArrayList<>());
        }
        return cartResult;
    }

    public CheckoutResponse checkoutCart(int userId) {
        User user = userRepository.getById(userId);
        Optional<Cart> userCart = cartRepository.findByUserId(userId);

        // Refactor: Use Success/Failure here instead of returning void from service
        int orderId = orderService.createOrderAndOrderItem(user, userCart.get().getCartItems());
        cartRepository.delete(userCart.get());

        return new CheckoutResponse(userId, orderId, "Successfully checked out");
    }
}
