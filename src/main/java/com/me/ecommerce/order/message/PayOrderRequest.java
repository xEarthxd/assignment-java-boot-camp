package com.me.ecommerce.order.message;

import com.me.ecommerce.shared_components.model.CardInfo;

public class PayOrderRequest {
    private int userId;
    private int orderId;
    private String paymentChannel;
    private CardInfo CardInfo;
    private float amount;

    public PayOrderRequest() {
    }

    public PayOrderRequest(int userId, int orderId, String paymentChannel, CardInfo cardInfo, float amount) {
        this.userId = userId;
        this.orderId = orderId;
        this.paymentChannel = paymentChannel;
        this.CardInfo = cardInfo;
        this.amount = amount;
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

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public CardInfo getCardInfo() {
        return CardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.CardInfo = cardInfo;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
