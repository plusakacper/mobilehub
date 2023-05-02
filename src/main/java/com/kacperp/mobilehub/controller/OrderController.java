package com.kacperp.mobilehub.controller;

import com.kacperp.mobilehub.dto.OrderDto;
import com.kacperp.mobilehub.form.OrderForm;
import com.kacperp.mobilehub.model.Order;
import com.kacperp.mobilehub.model.OrderProduct;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.service.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController extends BaseController {
    private final IOrderService orderService;
    private final IEmailService emailService;

    @Autowired
    public OrderController(IProductCategoryService productCategoryService, IOrderProductService orderProductService,
                           IUserService userService, IOrderService orderService, IEmailService emailService) {
        super(productCategoryService, orderProductService, userService);
        this.orderService = orderService;
        this.emailService = emailService;
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        super.getProductCategories(model);
        List<OrderDto> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/order/user")
    @Transactional
    public String orders(Model model, Principal principal) {
        super.getProductCategories(model);
        User user = super.getLoggedUser(principal);
        if(user != null) {
            List<OrderDto> orders = orderService.findByUser(user);
            model.addAttribute("orders", orders);
            return "orders";
        }
        return "home";
    }

    @GetMapping("/order/checkout")
    public String checkout(Model model, Principal principal) {
        super.getProductCategories(model);
        OrderForm orderForm = new OrderForm();
        orderForm.setTotal(orderProductService.calculateOrderProductsTotal(super.getLoggedUserCart(principal)));
        model.addAttribute("orderForm", orderForm);
        model.addAttribute("cartProducts", super.getLoggedUserCartDto(principal));

        return "checkout";
    }

    @PostMapping("/order/checkout")
    public String checkoutAction(@Valid @ModelAttribute("orderForm") OrderForm orderForm,
                                 BindingResult result, Model model, Principal principal) {
        super.getProductCategories(model);
        if (result.hasErrors()) {
            model.addAttribute("cartProducts", super.getLoggedUserCartDto(principal));
            orderForm.setTotal(orderProductService.calculateOrderProductsTotal(super.getLoggedUserCart(principal)));
            model.addAttribute("orderForm", orderForm);
            return "checkout";
        }
        User user = super.getLoggedUser(principal);
        List<OrderProduct> cartProducts = super.getLoggedUserCart(principal);
        if(user != null && cartProducts.size() > 0) {
            orderForm.setCartProducts(cartProducts);
            orderForm.setUser(user);
            Order savedOrder = orderService.save(orderForm);
            if(savedOrder != null) {
                emailService.sendOrderConfirmationEmail(user, savedOrder);
                return "redirect:/order/" + savedOrder.getId();
            }
            return "redirect:/home/";
        }
        return "redirect:/home/";
    }

    @GetMapping("/order/{id}")
    public String orderDetails(@PathVariable("id") Long id, Model model, Principal principal) {
        super.getProductCategories(model);
        OrderDto order = orderService.findById(id);
        model.addAttribute("order", order);
        return "order";
    }

    @DeleteMapping("orders/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        orderService.delete(id);
        return "redirect:/orders";
    }
}
