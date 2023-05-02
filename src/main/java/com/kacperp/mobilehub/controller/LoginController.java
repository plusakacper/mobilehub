package com.kacperp.mobilehub.controller;

import com.kacperp.mobilehub.dto.UserDto;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.service.IOrderProductService;
import com.kacperp.mobilehub.service.IProductCategoryService;
import com.kacperp.mobilehub.service.IUserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class LoginController extends BaseController {
    @Autowired
    public LoginController(IProductCategoryService productCategoryService, IOrderProductService orderProductService,
                          IUserService userService) {
        super(productCategoryService, orderProductService, userService);
    }

    @GetMapping("/login")
    public String login(Model model) {
        super.getProductCategories(model);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        super.getProductCategories(model);
        UserDto user = new UserDto();
        model.addAttribute("user", user);

        return "register";
    }

    @PostMapping("/register")
    public String registerAction(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model) {
        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            result.rejectValue("email", null, "Email is already used.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.save(user);
        return "redirect:/register?success";
    }
}
