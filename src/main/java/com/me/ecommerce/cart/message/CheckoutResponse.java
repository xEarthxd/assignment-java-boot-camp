package com.me.ecommerce.cart.message;

public class CheckoutResponse {
    private int userId;
    private int orderId;
    private String message;

    public CheckoutResponse() {
    }

    public CheckoutResponse(int userId, int orderId, String message) {
        this.userId = userId;
        this.orderId = orderId;
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
