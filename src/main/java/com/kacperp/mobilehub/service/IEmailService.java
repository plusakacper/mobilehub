package com.kacperp.mobilehub.service;

import com.kacperp.mobilehub.model.Order;
import com.kacperp.mobilehub.model.User;

public interface IEmailService {
    void sendOrderConfirmationEmail(User user, Order order);
}
