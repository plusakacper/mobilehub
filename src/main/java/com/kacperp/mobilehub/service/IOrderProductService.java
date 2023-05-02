package com.kacperp.mobilehub.service;

import com.kacperp.mobilehub.dto.OrderProductDto;
import com.kacperp.mobilehub.model.OrderProduct;
import com.kacperp.mobilehub.model.Product;
import com.kacperp.mobilehub.model.User;

import java.util.List;

public interface IOrderProductService {
    OrderProductDto findById(Long id);
    List<OrderProductDto> findAll();
    OrderProductDto convertEntityToDto(OrderProduct orderProduct);
    OrderProduct save(OrderProduct orderProduct);
    void delete(Long id);
    void addProductToCart(User user, Product product, Integer quantity);
    double calculateOrderProductsTotal(List<OrderProduct> orderProducts);
}
