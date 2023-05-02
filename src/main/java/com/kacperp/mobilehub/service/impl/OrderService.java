package com.kacperp.mobilehub.service.impl;

import com.kacperp.mobilehub.dto.OrderDto;
import com.kacperp.mobilehub.dto.OrderProductDto;
import com.kacperp.mobilehub.form.OrderForm;
import com.kacperp.mobilehub.model.Order;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.repository.OrderRepository;
import com.kacperp.mobilehub.service.IOrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderProductService orderProductService) {
        this.orderRepository = orderRepository;
        this.orderProductService = orderProductService;
    }

    @Override
    public OrderDto findById(Long id)  {
        Optional<Order> order = orderRepository.findById(id);

        if(order.isPresent()) {
            return convertEntityToDto(order.get());
        } else {
            return null;
        }
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> convertEntityToDto(order))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @Override
    public List<OrderDto> findByUser(User user) {
        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(order -> convertEntityToDto(order))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public OrderDto convertEntityToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        String address = order.getAddress().replaceAll("(\r\n|\n)", "<br />");
        orderDto.setAddress(address);
        orderDto.setComments(order.getComments());
        orderDto.setCreatedAt(order.getCreatedAt());

        List<OrderProductDto> orderProductsDto =  order.getOrderItemList().stream()
                .map(orderProduct -> orderProductService.convertEntityToDto(orderProduct))
                .collect(Collectors.toCollection(ArrayList::new));

        orderDto.setOrderItemList(orderProductsDto);
        orderDto.setUser(order.getUser());
        orderDto.setNumber(order.getNumber());
        orderDto.setTotal(orderProductService.calculateOrderProductsTotal(order.getOrderItemList()));

        return orderDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()) {
            orderRepository.delete(order.get());
        }
    }

    @Override
    @Transactional
    public Order save(OrderForm orderForm) {
        Order order = new Order();
        order.setUser(orderForm.getUser());
        order.setOrderItemList(orderForm.getCartProducts());
        order.setComments(orderForm.getComments());
        order.setAddress(formatAddress(orderForm));
        UUID uuid = UUID.randomUUID();
        order.setNumber(uuid + "");
        Order savedOrder = orderRepository.save(order);
        orderForm.getCartProducts().stream().forEach(orderProduct -> {
            orderProduct.setOrder(savedOrder);
            orderProductService.save(orderProduct);
        });
        return savedOrder;
    }
    public String formatAddress(OrderForm orderForm) {
        StringBuilder address = new StringBuilder();
        address.append("Name: ");
        address.append(orderForm.getFirstName());
        address.append(" ");
        address.append(orderForm.getLastName());
        address.append(System.lineSeparator());
        address.append("Email: ");
        address.append(orderForm.getEmail());
        address.append(System.lineSeparator());
        address.append("Phone: ");
        address.append(orderForm.getTelephone());
        address.append(System.lineSeparator());
        address.append("Address: ");
        address.append(orderForm.getCity());
        address.append(", ");
        address.append(orderForm.getStreet());
        address.append(" ");
        address.append(orderForm.getPropertyNumber());
        address.append(System.lineSeparator());
        address.append("Post code: ");
        address.append(orderForm.getPostalCode());
        return address.toString();
    }
}
