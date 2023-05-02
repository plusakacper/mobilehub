package com.kacperp.mobilehub.unit;

import com.kacperp.mobilehub.dto.OrderDto;
import com.kacperp.mobilehub.dto.OrderProductDto;
import com.kacperp.mobilehub.form.OrderForm;
import com.kacperp.mobilehub.model.*;
import com.kacperp.mobilehub.repository.OrderProductRepository;
import com.kacperp.mobilehub.repository.OrderRepository;
import com.kacperp.mobilehub.service.impl.OrderProductService;
import com.kacperp.mobilehub.service.impl.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    private OrderRepository orderRepository = mock(OrderRepository.class);
    private OrderProductRepository orderProductRepository = mock(OrderProductRepository.class);

    private OrderProductService orderProductService = new OrderProductService(orderProductRepository);
    private OrderService orderService = new OrderService(orderRepository, orderProductService);

    private User user;
    private ProductCategory productCategory;
    private Product product;
    private OrderProduct orderProduct;
    private Order order;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));

        productCategory = new ProductCategory();
        productCategory.setId(1L);
        productCategory.setDisplayName("Test Category");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice("100");
        product.setFilename("test_image.jpg");
        product.setActive(true);
        product.setProductCategory(productCategory);

        orderProduct = new OrderProduct();
        orderProduct.setId(1L);
        orderProduct.setProduct(product);
        orderProduct.setQuantity("1");
        orderProduct.setUser(user);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setAddress("Address line 1\nAddress line 2");
        order.setComments("No comments");
        order.setNumber("12345");
        order.setOrderItemList(Arrays.asList(orderProduct));
    }

    @Test
    void testFindAll() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        List<OrderDto> orderDtos = orderService.findAll();

        assertEquals(1, orderDtos.size());
        assertEquals(order.getId(), orderDtos.get(0).getId());
        assertEquals(order.getAddress(), orderDtos.get(0).getAddress().replaceAll("<br />", "\n"));
        assertEquals(order.getComments(), orderDtos.get(0).getComments());
        assertEquals(order.getNumber(), orderDtos.get(0).getNumber());

    }

    @Test
    void testFindById() {
        order.setOrderItemList(Arrays.asList(orderProduct));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        //when(orderProductService.convertEntityToDto(orderProduct)).thenReturn(new OrderProductDto());

        OrderDto orderDto = orderService.findById(1L);

        assertEquals(order.getId(), orderDto.getId());
        assertEquals(order.getAddress(), orderDto.getAddress().replaceAll("<br />", "\n"));
        assertEquals(order.getComments(), orderDto.getComments());
        assertEquals(order.getCreatedAt(), orderDto.getCreatedAt());
        assertEquals(order.getNumber(), orderDto.getNumber());
        assertEquals(order.getUser(), orderDto.getUser());
        assertEquals(order.getOrderItemList().size(), orderDto.getOrderItemList().size());
    }

    @Test
    void testFindByUser() {
        order.setOrderItemList(Arrays.asList(orderProduct));
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order));
        //when(orderProductService.convertEntityToDto(orderProduct)).thenReturn(new OrderProductDto());

        List<OrderDto> orderDtos = orderService.findByUser(user);

        assertEquals(1, orderDtos.size());
        assertEquals(order.getId(), orderDtos.get(0).getId());
        assertEquals(order.getAddress(), orderDtos.get(0).getAddress().replaceAll("<br />", "\n"));
        assertEquals(order.getComments(), orderDtos.get(0).getComments());
        assertEquals(order.getCreatedAt(), orderDtos.get(0).getCreatedAt());
        assertEquals(order.getNumber(), orderDtos.get(0).getNumber());
        assertEquals(order.getUser(), orderDtos.get(0).getUser());
        assertEquals(order.getOrderItemList().size(), orderDtos.get(0).getOrderItemList().size());
    }

    @Test
    void testDelete() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.delete(1L);

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void testSave() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderProductService.save(any(OrderProduct.class))).thenReturn(orderProduct);

        OrderForm orderForm = new OrderForm();
        orderForm.setUser(user);
        orderForm.setCartProducts(Arrays.asList(orderProduct));
        orderForm.setComments("No comments");
        orderForm.setFirstName("John");
        orderForm.setLastName("Doe");
        orderForm.setEmail("john@example.com");
        orderForm.setTelephone("123456789");
        orderForm.setCity("Test City");
        orderForm.setStreet("Test Street");
        orderForm.setPropertyNumber("123");
        orderForm.setPostalCode("12345");

        Order savedOrder = orderService.save(orderForm);

        assertNotNull(savedOrder);
        assertEquals(order.getUser(), savedOrder.getUser());
        assertEquals(order.getOrderItemList(), savedOrder.getOrderItemList());
        assertEquals(order.getComments(), savedOrder.getComments());
        assertEquals(order.getAddress(), savedOrder.getAddress());
    }
}
