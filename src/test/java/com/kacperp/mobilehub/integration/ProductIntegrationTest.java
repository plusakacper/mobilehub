package com.kacperp.mobilehub.integration;

import com.kacperp.mobilehub.dto.ProductDto;
import com.kacperp.mobilehub.form.ProductForm;
import com.kacperp.mobilehub.model.Product;
import com.kacperp.mobilehub.model.ProductCategory;
import com.kacperp.mobilehub.repository.ProductCategoryRepository;
import com.kacperp.mobilehub.repository.ProductRepository;
import com.kacperp.mobilehub.service.IProductService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class ProductIntegrationTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IProductService productService;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @AfterAll
    public void cleanUp() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @Test
    public void testFindAllActive() {
        // Add a product category
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Test Category");
        productCategory.setDisplayName("Test Category display name");
        productCategory = productCategoryRepository.save(productCategory);

        // Add a product
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice("99.99");
        product.setActive(true);
        product.setFilename("test_image.jpg");
        product.setProductCategory(productCategory); // Set the product category
        productRepository.save(product);

        // Add a product
        Product product2 = new Product();
        product2.setName("Test Product 2");
        product2.setDescription("Test Description 2");
        product2.setPrice("910.99");
        product2.setActive(false);
        product2.setFilename("test_image2.jpg");
        product2.setProductCategory(productCategory); // Set the product category
        productRepository.save(product2);

        // Test if findAllActive() works
        List<ProductDto> products = productService.findAllActive();
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(product.getName(), products.get(0).getName());
        assertEquals(product.getDescription(), products.get(0).getDescription());
    }

    @Test
    public void testCreateProduct() {
        // Add a product category
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Test Category");
        productCategory.setDisplayName("Test Category display name");
        productCategory = productCategoryRepository.save(productCategory);

        ProductForm productForm = new ProductForm();
        productForm.setName("New Product");
        productForm.setDescription("New Description");
        productForm.setPrice(new BigDecimal("199.99"));
        productForm.setCategoryId(productCategory.getId()); // Set the category ID
        productForm.setImageFilename("new_image.jpg");
        productForm.setActive(true);

        Product createdProduct = productService.save(productForm);
        assertNotNull(createdProduct);
        assertEquals(productForm.getName(), createdProduct.getName());
        assertEquals(productForm.getDescription(), createdProduct.getDescription());
    }

    @Test
    public void testFindById() {
        // Add a product category
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Test Category");
        productCategory.setDisplayName("Test Category display name");
        productCategory = productCategoryRepository.save(productCategory);

        // Add a product
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice("99.99");
        product.setActive(true);
        product.setFilename("test_image.jpg");
        product.setProductCategory(productCategory); // Set the product category
        Product savedProduct = productRepository.save(product);

        // Test findById() method
        ProductDto foundProduct = productService.findByIdAsDto(savedProduct.getId());
        assertNotNull(foundProduct);
        assertEquals(savedProduct.getName(), foundProduct.getName());
        assertEquals(savedProduct.getDescription(), foundProduct.getDescription());
    }

    @Test
    public void testUpdateProduct() {
        // Add a product category
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Test Category");
        productCategory.setDisplayName("Test Category display name");
        productCategory = productCategoryRepository.save(productCategory);

        // Add a product
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice("99.99");
        product.setActive(true);
        product.setFilename("test_image.jpg");
        product.setProductCategory(productCategory);
        Product savedProduct = productRepository.save(product);

        // Update the product
        ProductForm updatedProductForm = new ProductForm();
        updatedProductForm.setId(savedProduct.getId());
        updatedProductForm.setName("Updated Product");
        updatedProductForm.setDescription("Updated Description");
        updatedProductForm.setPrice(new BigDecimal("149.99"));
        updatedProductForm.setCategoryId(productCategory.getId());
        updatedProductForm.setImageFilename("updated_image.jpg");
        updatedProductForm.setActive(true);

        Product updatedProduct = productService.update(updatedProductForm, savedProduct);
        assertNotNull(updatedProduct);
        assertEquals(updatedProductForm.getName(), updatedProduct.getName());
        assertEquals(updatedProductForm.getDescription(), updatedProduct.getDescription());
    }

}
