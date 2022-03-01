package com.me.ecommerce.order.message;

import com.me.ecommerce.shared_components.model.ItemInfo;

import java.util.List;

public class ViewOrderResponse {
    private int userId;
    private int orderid;
    private String orderStatus;
    private int count;
    private List<ItemInfo> items;
    private float amount;

    public ViewOrderResponse() {

    }

    public ViewOrderResponse(int userId, int orderId, String orderStatus, int count, List<ItemInfo> items, float amount) {
        this.userId = userId;
        this.orderid = orderId;
        this.orderStatus = orderStatus;
        this.count = count;
        this.items = items;
        this.amount = amount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
