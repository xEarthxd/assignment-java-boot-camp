package com.me.ecommerce.cart.model;

import com.me.ecommerce.user.model.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @Column(name="created_at")
    private Timestamp createdAt;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;

    public Cart() {}

    public Cart(User user, Timestamp createdAt) {
        this.user = user;
        this.createdAt = createdAt;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
