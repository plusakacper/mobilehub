package com.kacperp.mobilehub.controller;

import com.kacperp.mobilehub.dto.OrderProductDto;
import com.kacperp.mobilehub.dto.ProductCategoryDto;
import com.kacperp.mobilehub.model.OrderProduct;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.service.IOrderProductService;
import com.kacperp.mobilehub.service.IProductCategoryService;
import com.kacperp.mobilehub.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class BaseController {
    protected final IProductCategoryService productCategoryService;
    protected final IOrderProductService orderProductService;
    protected final IUserService userService;
    @Autowired
    public BaseController(IProductCategoryService productCategoryService, IOrderProductService orderProductService,
                          IUserService userService) {
        this.productCategoryService = productCategoryService;
        this.orderProductService = orderProductService;
        this.userService = userService;
    }
    public void getProductCategories(Model model) {
        List<ProductCategoryDto> productCategories = productCategoryService.findAll();
        model.addAttribute("productCategories", productCategories);
    }
    public List<OrderProductDto> getLoggedUserCartDto(Principal principal) {
        List<OrderProductDto> cartProducts = new ArrayList<>();
        if(principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                cartProducts = user.get().getCart().stream()
                    .filter(orderProduct -> orderProduct.getOrder() == null)
                    .map(orderProduct -> orderProductService.convertEntityToDto(orderProduct))
                    .collect(Collectors.toList());
            }
        }
        return cartProducts;
    }
    public List<OrderProduct> getLoggedUserCart(Principal principal) {
        List<OrderProduct> cartProducts = new ArrayList<>();
        if(principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                cartProducts = user.get().getCart().stream()
                        .filter(orderProduct -> orderProduct.getOrder() == null)
                        .collect(Collectors.toList());
            }
        }
        return cartProducts;
    }
    public User getLoggedUser(Principal principal) {
        User loggedUser = null;
        if(principal != null) {
            Optional<User> userFromDb = userService.findByEmail(principal.getName());
            if (userFromDb.isPresent()) {
               loggedUser = userFromDb.get();
            }
        }
        return loggedUser;
    }
}
