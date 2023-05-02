package com.kacperp.mobilehub.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String number;
    @Column(length = 500)
    private String comments;
    @Column(nullable = false)
    private String address;
    @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderProduct> orderItemList;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public List<OrderProduct> getOrderItemList() {
        return orderItemList;
    }
    public void setOrderItemList(List<OrderProduct> orderItemList) {
        this.orderItemList = orderItemList;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
}
