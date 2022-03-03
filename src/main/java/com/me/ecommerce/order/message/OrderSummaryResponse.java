package com.me.ecommerce.order.message;

import com.me.ecommerce.shared_components.model.ItemInfo;

import java.util.List;

public class OrderSummaryResponse {
    private String invoiceNo;
    private String name;
    private String shippingAddress;
    private String transactionDate;
    private float amount;
    private int count;
    private List<ItemInfo> items;

    public OrderSummaryResponse() {
    }

    public OrderSummaryResponse(String invoiceNo, String name, String shippingAddress, String transactionDate, float amount, int count, List<ItemInfo> items) {
        this.invoiceNo = invoiceNo;
        this.name = name;
        this.shippingAddress = shippingAddress;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.count = count;
        this.items = items;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
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
