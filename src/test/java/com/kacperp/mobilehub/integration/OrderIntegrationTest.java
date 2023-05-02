package com.kacperp.mobilehub.integration;

import com.kacperp.mobilehub.dto.OrderDto;
import com.kacperp.mobilehub.form.OrderForm;
import com.kacperp.mobilehub.model.*;
import com.kacperp.mobilehub.model.Order;
import com.kacperp.mobilehub.repository.*;
import com.kacperp.mobilehub.service.IOrderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class OrderIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private IOrderService orderService;

    @BeforeEach
    public void setUp() {
        orderProductRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void cleanUp() {
        orderProductRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test createOrder: Ensure an order is correctly created and saved")
    public void testCreateOrder() {
        // Add a user
        User user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        // Add a product category
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Test Category");
        productCategory.setDisplayName("Test Category display name");
        productCategory = productCategoryRepository.save(productCategory);

        // Add a product
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice("10.99");
        product.setFilename("test_image.jpg");
        product.setProductCategory(productCategory); // Set the product category
        product = productRepository.save(product);

        // Add an order product
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setUser(user);
        orderProduct.setQuantity("2");

        // Create an order form
        OrderForm orderForm = new OrderForm();
        orderForm.setFirstName("John");
        orderForm.setLastName("Doe");
        orderForm.setCity("New York");
        orderForm.setStreet("5th Avenue");
        orderForm.setPropertyNumber("10");
        orderForm.setPostalCode("12345");
        orderForm.setEmail("john.doe@example.com");
        orderForm.setTelephone("555-1234");
        orderForm.setComments("Test comment");
        orderForm.setCartProducts(Arrays.asList(orderProduct));
        orderForm.setUser(user);

        // Save the order
        Order savedOrder = orderService.save(orderForm);

        // Verify the saved order
        assertNotNull(savedOrder);
        assertEquals(orderForm.getComments(), savedOrder.getComments());

        // Verify the user in the saved order
        assertEquals(user.getId(), savedOrder.getUser().getId());

        assertEquals(1, savedOrder.getOrderItemList().size());
        OrderProduct savedOrderProduct = savedOrder.getOrderItemList().get(0);
        assertEquals(orderProduct.getProduct().getId(), savedOrderProduct.getProduct().getId());
        assertEquals(orderProduct.getUser().getId(), savedOrderProduct.getUser().getId());
        assertEquals(orderProduct.getQuantity(), savedOrderProduct.getQuantity());
    }

    @Test
    @Transactional
    @DisplayName("Test findByUser: Ensure orders are correctly retrieved for specific user")
    public void testFindByUser() {
        // Add a user
        User user = new User();
        user.setFirstname("Test name");
        user.setLastname("Test lastname");
        user.setEmail("test@example.com");
        user.setPassword("testpassword");
        user = userRepository.save(user);

        // Add a user
        User user2 = new User();
        user2.setFirstname("Joane");
        user2.setLastname("Sit");
        user2.setEmail("Joane sit@example.com");
        user2.setPassword("testpassword2");
        user2 = userRepository.save(user2);

        // Add a product category
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Test Category");
        productCategory.setDisplayName("Test Category display name");
        productCategory = productCategoryRepository.save(productCategory);

        // Add a product
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice("100.09");
        product.setFilename("test_image.jpg");
        product.setProductCategory(productCategory); // Set the product category
        product = productRepository.save(product);

        // Add an order product
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setUser(user);
        orderProduct.setQuantity("4");

        // Add an order
        Order order = new Order();
        order.setNumber("12345");
        order.setComments("Test comment");
        order.setAddress("Test address");
        order.setUser(user);
        order.setOrderItemList((Arrays.asList(orderProduct)));
        orderRepository.save(order);

        // Test if findByUser() works
        List<OrderDto> orders = orderService.findByUser(user);
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order.getNumber(), orders.get(0).getNumber());
        assertEquals(order.getComments(), orders.get(0).getComments());
    }
}
