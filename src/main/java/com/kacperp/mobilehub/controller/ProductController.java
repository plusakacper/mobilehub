package com.kacperp.mobilehub.controller;

import com.kacperp.mobilehub.dto.ProductDto;
import com.kacperp.mobilehub.form.ProductForm;
import com.kacperp.mobilehub.model.Product;
import com.kacperp.mobilehub.service.IOrderProductService;
import com.kacperp.mobilehub.service.IProductCategoryService;
import com.kacperp.mobilehub.service.IProductService;

import com.kacperp.mobilehub.service.IUserService;
import com.kacperp.mobilehub.util.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController extends BaseController {
    private final IProductService productService;

    @Value("${files.directory.path}")
    private String filesDirectoryPath;

    @Autowired
    public ProductController(IProductCategoryService productCategoryService, IOrderProductService orderProductService,
                             IUserService userService, IProductService productService) {
        super(productCategoryService, orderProductService, userService);
        this.productService = productService;
    }

    @GetMapping("/products")
    public String activeProducts(Model model) {
        super.getProductCategories(model);
        List<ProductDto> products = productService.findAllActive();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/products/category/{id}")
    public String activeProductsByCategory(@PathVariable("id") Long id, Model model) {
        super.getProductCategories(model);
        List<ProductDto> products = productService.findActiveByProductCategoryId(id);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String activeProductDetails(@PathVariable("id") Long id, Model model) {
        super.getProductCategories(model);
        ProductDto product = productService.findActiveByIdAsDto(id);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/products/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = new FileSystemResource(filesDirectoryPath + filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE).body(file);
    }

    @GetMapping("/products/manage")
    public String allProducts(Model model) {
        super.getProductCategories(model);
        List<ProductDto> products = productService.findAll();
        model.addAttribute("products", products);
        return "manage-products/list";
    }

    @GetMapping("/products/manage/{id}")
    public String productDetails(@PathVariable("id") Long id, Model model) {
        super.getProductCategories(model);
        ProductDto product = productService.findByIdAsDto(id);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/products/manage/add")
    public String addProduct(Model model) {
        super.getProductCategories(model);
        ProductForm productForm = new ProductForm();
        productForm.setActive(true);
        model.addAttribute("productForm", productForm);

        return "manage-products/add";
    }

    @GetMapping("/products/manage/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        super.getProductCategories(model);

        Product product = productService.findByIdAsEntity(id);
        if (product == null) {
            return "redirect:/products/manage";
        }

        ProductForm productForm = productService.convertEntityToProductForm(product);
        model.addAttribute("productForm", productForm);

        return "manage-products/edit";
    }

    @RequestMapping(value="/products/manage/add", method=RequestMethod.POST)
    public String addProduct(@RequestParam(value ="productImage", required=false, defaultValue="") MultipartFile productImage,
            @Valid @ModelAttribute("productForm") ProductForm productForm, BindingResult result, Model model, Principal principal) {

        super.getProductCategories(model);
        if (result.hasErrors() || productImage.getSize() == 0) {
            if (productImage.getSize() == 0) {
                result.rejectValue("imageFilename", "error.imageFilename", "Please select product image");
            }
            model.addAttribute("productForm", productForm);
            return "manage-products/add";
        }

        String imageFilename = Utils.saveImageToDisk(filesDirectoryPath, productImage);
        productForm.setImageFilename(imageFilename);

        Product savedProduct = productService.save(productForm);
        return "redirect:/products/manage";
    }

    @PostMapping("/products/manage/edit")
    public String editProduct(@RequestParam(value ="productImage", required=false, defaultValue="") MultipartFile productImage,
                              @Valid @ModelAttribute("productForm") ProductForm productForm, BindingResult result, Model model) {

        super.getProductCategories(model);
        if (result.hasErrors()) {
            model.addAttribute("productForm", productForm);
            return "manage-products/edit";
        }

        Product product = productService.findByIdAsEntity(productForm.getId());
        if (product == null) {
            return "redirect:/products/manage";
        }

        // Save the image if a new one was uploaded
        if (productImage.getSize() > 0) {
            String imageFilename = Utils.saveImageToDisk(filesDirectoryPath, productImage);
            productForm.setImageFilename(imageFilename);
        }

        // Update the product with the new information
        productService.update(productForm, product);

        return "redirect:/products/manage";
    }

    @PostMapping("/products/manage/archive/{id}")
    public String archiveProduct(@PathVariable("id") Long id) {
        Product product = productService.findByIdAsEntity(id);
        if (product != null) {
            product.setActive(false);
            productService.save(product);
        }
        return "redirect:/products/manage";
    }

    @PostMapping("/products/manage/unarchive/{id}")
    public String unarchiveProduct(@PathVariable("id") Long id) {
        Product product = productService.findByIdAsEntity(id);
        if (product != null) {
            product.setActive(true);
            productService.save(product);
        }
        return "redirect:/products/manage";
    }
}
