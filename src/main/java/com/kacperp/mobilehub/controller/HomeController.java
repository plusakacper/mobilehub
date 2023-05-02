package com.kacperp.mobilehub.controller;

import com.kacperp.mobilehub.service.IOrderProductService;
import com.kacperp.mobilehub.service.IProductCategoryService;
import com.kacperp.mobilehub.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends BaseController {
    private final IUserService userService;

    @Autowired
    public HomeController(IProductCategoryService productCategoryService, IOrderProductService orderProductService,
                          IUserService userService) {
        super(productCategoryService, orderProductService, userService);
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        super.getProductCategories(model);
        return "home";
    }

}
