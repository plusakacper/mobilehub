package com.kacperp.mobilehub.controller;

import com.kacperp.mobilehub.dto.UserDto;
import com.kacperp.mobilehub.service.IOrderProductService;
import com.kacperp.mobilehub.service.IProductCategoryService;
import com.kacperp.mobilehub.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class UserController extends BaseController {
    @Autowired
    public UserController(IProductCategoryService productCategoryService, IOrderProductService orderProductService,
                          IUserService userService) {
        super(productCategoryService, orderProductService, userService);
    }

    @GetMapping("/users")
    public String users(Model model) {
        super.getProductCategories(model);
        List<UserDto> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @DeleteMapping("users/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
