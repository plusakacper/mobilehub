package com.kacperp.mobilehub.dto;

import com.kacperp.mobilehub.model.Order;
import com.kacperp.mobilehub.model.Product;
import com.kacperp.mobilehub.model.User;

public class OrderProductDto {
    private Long id;
    private String quantity;
    private Order order;
    private Product product;
    private User user;
    private String img64Base;
    private String subtotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getImg64Base() {
        return img64Base;
    }
    public void setImg64Base(String img64Base) {
        this.img64Base = img64Base;
    }
    public String getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
}
