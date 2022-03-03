package com.me.ecommerce.order.model;

import com.me.ecommerce.product.model.Product;
import com.me.ecommerce.shared_components.model.ItemInfo;
import com.me.ecommerce.user.model.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="order_t")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public Order() {
    }

    public Order(User user, String status, Timestamp createdAt, Timestamp modifiedAt) {
        this.user = user;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public float getTotalAmount() {
        float totalAmount = 0.0f;
        for (OrderItem item : this.orderItems) {
            totalAmount += item.getProduct().getPrice()*item.getQuantity();
        }
        return (float) (Math.round(totalAmount * 100.0) / 100.0);
    }

    public List<ItemInfo> getItemInfo() {
        List<ItemInfo> orderItemsInfo = new ArrayList<ItemInfo>();
        for (OrderItem item : this.orderItems) {
            Product product = item.getProduct();
            ItemInfo itemInfo = new ItemInfo(product.getId(), product.getName(), product.getPrice(), product.getDescription());
            orderItemsInfo.add(itemInfo);
        }
        return orderItemsInfo;
    }
}
