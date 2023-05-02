package com.kacperp.mobilehub.service.impl;

import com.kacperp.mobilehub.dto.OrderProductDto;
import com.kacperp.mobilehub.model.OrderProduct;
import com.kacperp.mobilehub.model.Product;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.repository.OrderProductRepository;
import com.kacperp.mobilehub.service.IOrderProductService;
import com.kacperp.mobilehub.util.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderProductService implements IOrderProductService {
    private final OrderProductRepository orderProductRepository;
    //@Value("${files.directory.path}")
    //private String filesDirectoryPath;

    @Autowired
    public OrderProductService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public OrderProductDto findById(Long id) {
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(id);

        if(orderProduct.isPresent()) {
            return convertEntityToDto(orderProduct.get());
        } else {
            return null;
        }
    }

    @Override
    public List<OrderProductDto> findAll() {
        List<OrderProduct> orderProducts = orderProductRepository.findAll();

        return orderProducts.stream()
                .map(orderProduct -> convertEntityToDto(orderProduct))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public OrderProductDto convertEntityToDto(OrderProduct orderProduct) {
        OrderProductDto orderProductDto = new OrderProductDto();
        orderProductDto.setId(orderProduct.getId());
        orderProductDto.setProduct(orderProduct.getProduct());
        orderProductDto.setOrder(orderProduct.getOrder());
        orderProductDto.setUser(orderProduct.getUser());
        orderProductDto.setQuantity(orderProduct.getQuantity());

        double subtotal = Double.parseDouble(orderProduct.getProduct().getPrice()) * Double.parseDouble(orderProduct.getQuantity());
        orderProductDto.setSubtotal(subtotal + "$");
        //String filePath = filesDirectoryPath + orderProduct.getProduct().getFilename();
        orderProductDto.setImg64Base(orderProduct.getProduct().getFilename());

        return orderProductDto;
    }


    @Override
    @Transactional
    public OrderProduct save(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(id);
        if(orderProduct.isPresent()) {
            orderProductRepository.delete(orderProduct.get());
        }
    }
    @Override
    @Transactional
    public void addProductToCart(User user, Product product, Integer quantity) {
            Optional<OrderProduct> orderProduct = user.getCart()
                    .stream()
                    .filter(orderItem -> orderItem.getOrder() == null && orderItem.getProduct().getId() == product.getId())
                    .findFirst();
            if(orderProduct.isPresent()) {
                int newQuantity = Integer.valueOf(orderProduct.get().getQuantity());
                newQuantity += quantity;
                orderProduct.get().setQuantity(newQuantity+"");
                save(orderProduct.get());
            } else {
                OrderProduct newOrderProduct = new OrderProduct();
                newOrderProduct.setProduct(product);
                newOrderProduct.setQuantity(quantity+"");
                newOrderProduct.setUser(user);
                save(newOrderProduct);
            }
    }
    public double calculateOrderProductsTotal(List<OrderProduct> orderProducts) {
        double total = 0;
        for (OrderProduct orderProduct : orderProducts) {
            double subtotal = Double.parseDouble(orderProduct.getProduct().getPrice()) *  Integer. valueOf(orderProduct.getQuantity());
            total += subtotal;
        }
        return total;
    }
}
