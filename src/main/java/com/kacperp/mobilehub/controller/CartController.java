package com.kacperp.mobilehub.controller;

import com.kacperp.mobilehub.dto.OrderProductDto;
import com.kacperp.mobilehub.model.Product;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.repository.ProductRepository;
import com.kacperp.mobilehub.service.IOrderProductService;
import com.kacperp.mobilehub.service.IProductCategoryService;
import com.kacperp.mobilehub.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class CartController extends BaseController {
    private final ProductRepository productRepository;

    @Autowired
    public CartController(IProductCategoryService productCategoryService, IOrderProductService orderProductService,
                          IUserService userService, ProductRepository productRepository) {
        super(productCategoryService, orderProductService, userService);
        this.productRepository = productRepository;

    }

    @GetMapping("/cart")
    public String userCart(Model model, Principal principal) {
        super.getProductCategories(model);
        List<OrderProductDto> cartProducts = super.getLoggedUserCartDto(principal);
        model.addAttribute("cartProducts", cartProducts);
        model.addAttribute("total", orderProductService.calculateOrderProductsTotal(super.getLoggedUserCart(principal)));
        return "cart";
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable("id") Long id, Principal principal,
                            @RequestParam(value ="quantity", required=false, defaultValue="1") Integer quantity) {
        User user = super.getLoggedUser(principal);
        if(user != null) {
            Optional<Product> product = productRepository.findById(id);
            if(product.isPresent()) {
                orderProductService.addProductToCart(user, product.get(), quantity);
            }
        }
        return "redirect:/cart";
    }

    @DeleteMapping("cart/delete/{id}")
    public String deleteFromCart(@PathVariable("id") Long id) {
        orderProductService.delete(id);
        return "redirect:/cart";
    }
}
