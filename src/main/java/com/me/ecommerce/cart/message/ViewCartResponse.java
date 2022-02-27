package com.me.ecommerce.cart.message;


import org.springframework.stereotype.Component;

import java.util.List;

public class ViewCartResponse {
    private int userId;
    private int cartId;
    private int count;
    private List<ItemInfo> items;

    public ViewCartResponse() {};

    public ViewCartResponse(int userId, int cartId, int count, List<ItemInfo> items) {
        this.userId = userId;
        this.cartId = cartId;
        this.count = count;
        this.items = items;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ItemInfo> getItems() {
        return items;
    }

    public void setItems(List<ItemInfo> items) {
        this.items = items;
    }
}