package com.kacperp.mobilehub.dto;

import com.kacperp.mobilehub.model.User;

import java.util.Date;
import java.util.List;

public class OrderDto {
    private Long id;
    private String number;
    private String comments;
    private String address;
    private User user;
    private List<OrderProductDto> orderItemList;
    private Date createdAt;
    private double total;

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
    public List<OrderProductDto> getOrderItemList() {
        return orderItemList;
    }
    public void setOrderItemList(List<OrderProductDto> orderItemList) {
        this.orderItemList = orderItemList;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
}
