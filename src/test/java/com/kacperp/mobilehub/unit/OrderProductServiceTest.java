package com.kacperp.mobilehub.unit;

import com.kacperp.mobilehub.dto.OrderProductDto;
import com.kacperp.mobilehub.model.OrderProduct;
import com.kacperp.mobilehub.model.Product;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.repository.OrderProductRepository;
import com.kacperp.mobilehub.service.impl.OrderProductService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class OrderProductServiceTest {

    private OrderProductRepository orderProductRepository = mock(OrderProductRepository.class);

    private OrderProductService orderProductService = new OrderProductService(orderProductRepository);

    @Test
    void testFindById() {
        Product product = new Product();
        product.setPrice("10.0");
        product.setFilename("test_image.png");

        User user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(1L);
        orderProduct.setOrder(null);
        orderProduct.setUser(user);
        orderProduct.setQuantity("2");
        orderProduct.setProduct(product);
        when(orderProductRepository.findById(1L)).thenReturn(Optional.of(orderProduct));

        OrderProductDto result = orderProductService.findById(1L);
        assertNotNull(result);
        assertEquals(orderProduct.getId(), result.getId());
        assertEquals(product.getId(), result.getProduct().getId());
        assertEquals(product.getFilename(), result.getProduct().getFilename());
    }

    @Test
    void testFindById_notFound() {
        when(orderProductRepository.findById(1L)).thenReturn(Optional.empty());
        OrderProductDto result = orderProductService.findById(1L);
        assertNull(result);
    }

    @Test
    void testFindAll() {
        User user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));

        Product product = new Product();
        product.setPrice("10.0");
        product.setFilename("test_image.png");

        Product product2 = new Product();
        product2.setPrice("10.0");
        product2.setFilename("test_image2.png");

        List<OrderProduct> orderProducts = new ArrayList<>();
        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setId(1L);
        orderProduct1.setProduct(product);
        orderProduct1.setOrder(null);
        orderProduct1.setUser(user);
        orderProduct1.setQuantity("2");
        orderProducts.add(orderProduct1);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setId(2L);
        orderProduct2.setProduct(product2);
        orderProduct2.setOrder(null);
        orderProduct2.setUser(user);
        orderProduct2.setQuantity("1");
        orderProducts.add(orderProduct2);

        when(orderProductRepository.findAll()).thenReturn(orderProducts);

        List<OrderProductDto> result = orderProductService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product.getId(), result.get(0).getProduct().getId());
        assertEquals(product.getFilename(), result.get(0).getProduct().getFilename());
        assertEquals(product2.getId(), result.get(1).getProduct().getId());
        assertEquals(product2.getFilename(), result.get(1).getProduct().getFilename());
    }

    @Test
    void testConvertEntityToDto() {
        Product product = new Product();
        product.setPrice("10.0");
        product.setFilename("test_image.png");

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(1L);
        orderProduct.setProduct(product);
        orderProduct.setQuantity("2");

        OrderProductDto result = orderProductService.convertEntityToDto(orderProduct);
        assertNotNull(result);
        assertEquals(orderProduct.getId(), result.getId());
        assertEquals("20.0$", result.getSubtotal());
        assertEquals(product.getFilename(), result.getImg64Base());
    }

    @Test
    void testSave() {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(1L);
        when(orderProductRepository.save(orderProduct)).thenReturn(orderProduct);

        OrderProduct result = orderProductService.save(orderProduct);
        assertNotNull(result);
        assertEquals(orderProduct.getId(), result.getId());
    }

    @Test
    void testDelete() {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(1L);
        when(orderProductRepository.findById(1L)).thenReturn(Optional.of(orderProduct));

        orderProductService.delete(1L);
        verify(orderProductRepository, times(1)).delete(orderProduct);
    }

    @Test
    void testDelete_notFound() {
        when(orderProductRepository.findById(1L)).thenReturn(Optional.empty());

        orderProductService.delete(1L);
        verify(orderProductRepository, times(0)).delete(any(OrderProduct.class));
    }

    @Test
    void testAddProductToCart_existingProduct() {
        User user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setCart(new ArrayList<>()); // Initialize the cart list
        Product product = new Product();
        product.setId(1L);

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(1L);
        orderProduct.setProduct(product);
        orderProduct.setQuantity("1");
        user.getCart().add(orderProduct);

        when(orderProductRepository.save(orderProduct)).thenReturn(orderProduct);

        orderProductService.addProductToCart(user, product, 1);

        assertEquals("2", orderProduct.getQuantity());
        verify(orderProductRepository, times(1)).save(orderProduct);
    }

    @Test
    void testAddProductToCart_newProduct() {
        User user = new User();
        user.setCart(new ArrayList<>()); // Initialize the cart list
        Product product = new Product();
        product.setId(1L);

        when(orderProductRepository.save(any(OrderProduct.class))).thenAnswer(invocation -> {
            OrderProduct savedOrderProduct = invocation.getArgument(0, OrderProduct.class);
            user.getCart().add(savedOrderProduct);
            return savedOrderProduct;
        });

        orderProductService.addProductToCart(user, product, 1);

        assertEquals(1, user.getCart().size());
        OrderProduct newOrderProduct = user.getCart().get(0);
        assertEquals(product, newOrderProduct.getProduct());
        assertEquals("1", newOrderProduct.getQuantity());
        verify(orderProductRepository, times(1)).save(newOrderProduct);
    }

    @Test
    void testCalculateOrderProductsTotal() {
        Product product1 = new Product();
        product1.setPrice("10.0");
        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setProduct(product1);
        orderProduct1.setQuantity("2");

        Product product2 = new Product();
        product2.setPrice("20.0");
        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setProduct(product2);
        orderProduct2.setQuantity("3");

        List<OrderProduct> orderProducts = new ArrayList<>();
        orderProducts.add(orderProduct1);
        orderProducts.add(orderProduct2);

        double result = orderProductService.calculateOrderProductsTotal(orderProducts);

        assertEquals(80.0, result);
    }
}
