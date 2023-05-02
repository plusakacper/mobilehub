package com.kacperp.mobilehub.service;

import com.kacperp.mobilehub.dto.OrderDto;
import com.kacperp.mobilehub.form.OrderForm;
import com.kacperp.mobilehub.model.Order;
import com.kacperp.mobilehub.model.User;

import java.util.List;

public interface IOrderService {
    OrderDto findById(Long id);
    List<OrderDto> findAll();
    List<OrderDto> findByUser(User user);
    OrderDto convertEntityToDto(Order order);
    void delete(Long id);
    Order save(OrderForm orderForm);
}
