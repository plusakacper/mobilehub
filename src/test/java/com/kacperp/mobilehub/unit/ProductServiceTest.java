package com.kacperp.mobilehub.unit;

import com.kacperp.mobilehub.dto.ProductDto;
import com.kacperp.mobilehub.form.ProductForm;
import com.kacperp.mobilehub.model.Product;
import com.kacperp.mobilehub.model.ProductCategory;
import com.kacperp.mobilehub.repository.ProductCategoryRepository;
import com.kacperp.mobilehub.repository.ProductRepository;
import com.kacperp.mobilehub.service.impl.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {

    private ProductRepository productRepository = mock(ProductRepository.class);
    private ProductCategoryRepository productCategoryRepository = mock(ProductCategoryRepository.class);

    private ProductService productService = new ProductService(productRepository, productCategoryRepository);

    @Value("${files.directory.path}")
    private String filesDirectoryPath;

    private Product product;
    private Product product2;
    private ProductCategory productCategory;
    private ProductForm productForm;
    private ProductDto productDto;
    private List<Product> productList;

    @BeforeEach
    void setUp() {
        productCategory = new ProductCategory();
        productCategory.setId(1L);
        productCategory.setDisplayName("Test Category");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice("100");
        product.setFilename("test_image.jpg");
        product.setActive(true);
        product.setProductCategory(productCategory);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Test Product 2");
        product2.setDescription("Test Description 2");
        product2.setPrice("200.00");
        product2.setFilename("test_image2.jpg");
        product2.setActive(false);
        product2.setProductCategory(productCategory);

        productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        productForm = new ProductForm();
        productForm.setId(1L);
        productForm.setName("Test Product");
        productForm.setDescription("Test Description");
        productForm.setPrice(new BigDecimal("100"));
        productForm.setCategoryId(1L);
        productForm.setImageFilename("test_image.jpg");
        productForm.setActive(true);

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice("$100");
        productDto.setFilename("test_image.jpg");
        productDto.setActive("Yes");
        productDto.setCategoryName("Test Category");
    }

    @Test
    void testFindByIdAsDto() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        ProductDto result = productService.findByIdAsDto(product.getId());

        assertNotNull(result);
        assertEquals(productDto.getId(), result.getId());
        assertEquals(productDto.getName(), result.getName());
        assertEquals(productDto.getDescription(), result.getDescription());
        assertEquals(productDto.getPrice(), result.getPrice());
        assertEquals(productDto.getActive(), result.getActive());
        assertEquals(productDto.getCategoryName(), result.getCategoryName());
        assertEquals(productDto.getFilename(), result.getFilename());
    }

    @Test
    void testFindByIdAsEntity() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product result = productService.findByIdAsEntity(product.getId());

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getActive(), result.getActive());
        assertEquals(product.getFilename(), result.getFilename());
        assertEquals(product.getProductCategory(), result.getProductCategory());
    }

    @Test
    void testFindAll() {
        when(productRepository.findAll()).thenReturn(productList);

        List<ProductDto> result = productService.findAll();
        assertEquals(2, result.size());
        assertEquals(productDto.getId(), result.get(0).getId());
        assertEquals(productDto.getName(), result.get(0).getName());
        assertEquals(productDto.getDescription(), result.get(0).getDescription());
        assertEquals(productDto.getPrice(), result.get(0).getPrice());
        assertEquals(productDto.getActive(), result.get(0).getActive());
        assertEquals(productDto.getCategoryName(), result.get(0).getCategoryName());
    }

    @Test
    void testFindAllActive() {
        when(productRepository.findAllByActiveTrue()).thenReturn(Arrays.asList(productList.get(0)));

        List<ProductDto> result = productService.findAllActive();
        assertEquals(1, result.size());
        assertEquals(productDto.getId(), result.get(0).getId());
        assertEquals(productDto.getName(), result.get(0).getName());
        assertEquals(productDto.getDescription(), result.get(0).getDescription());
        assertEquals(productDto.getPrice(), result.get(0).getPrice());
        assertEquals(productDto.getActive(), result.get(0).getActive());
        assertEquals(productDto.getCategoryName(), result.get(0).getCategoryName());
    }

    @Test
    void testFindByProductCategoryId() {
        when(productRepository.findByProductCategoryId(anyLong())).thenReturn(Arrays.asList(productList.get(0)));

        List<ProductDto> result = productService.findByProductCategoryId(1L);
        assertEquals(1, result.size());
        assertEquals(productDto.getId(), result.get(0).getId());
        assertEquals(productDto.getName(), result.get(0).getName());
        assertEquals(productDto.getDescription(), result.get(0).getDescription());
        assertEquals(productDto.getPrice(), result.get(0).getPrice());
        assertEquals(productDto.getActive(), result.get(0).getActive());
        assertEquals(productDto.getCategoryName(), result.get(0).getCategoryName());
    }

    @Test
    void testFindActiveByProductCategoryId() {
        when(productRepository.findByProductCategoryIdAndActiveTrue(anyLong())).thenReturn(Arrays.asList(productList.get(0)));

        List<ProductDto> result = productService.findActiveByProductCategoryId(1L);
        assertEquals(1, result.size());
        assertEquals(productDto.getId(), result.get(0).getId());
        assertEquals(productDto.getName(), result.get(0).getName());
        assertEquals(productDto.getDescription(), result.get(0).getDescription());
        assertEquals(productDto.getPrice(), result.get(0).getPrice());
        assertEquals(productDto.getActive(), result.get(0).getActive());
        assertEquals(productDto.getCategoryName(), result.get(0).getCategoryName());
    }

    @Test
    void testDelete() {
        when(productRepository.findByIdAndActiveTrue(anyLong())).thenReturn(Optional.of(product));

        productService.delete(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testSaveProductForm() {
        when(productCategoryRepository.findById(anyLong())).thenReturn(Optional.of(productCategory));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.save(productForm);
        assertEquals(product, result);
    }

    @Test
    void testSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.save(product);
        assertEquals(product, result);
    }

    @Test
    void testConvertEntityToDto() {
        ProductDto result = productService.convertEntityToDto(product);
        assertEquals(productDto.getId(), result.getId());
        assertEquals(productDto.getName(), result.getName());
        assertEquals(productDto.getDescription(), result.getDescription());
        assertEquals(productDto.getPrice(), result.getPrice());
        assertEquals(productDto.getActive(), result.getActive());
        assertEquals(productDto.getCategoryName(), result.getCategoryName());
        assertEquals(productDto.getFilename(), result.getFilename());
    }

    @Test
    void testConvertProductFormToEntity() {
        when(productCategoryRepository.findById(anyLong())).thenReturn(Optional.of(productCategory));

        Product result = productService.convertProductFormToEntity(productForm);
        assertEquals(null, result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getActive(), result.getActive());
        assertEquals(product.getFilename(), result.getFilename());
        assertEquals(product.getProductCategory(), result.getProductCategory());
    }

    @Test
    void testConvertEntityToProductForm() {
        ProductForm result = productService.convertEntityToProductForm(product);

        assertEquals(productForm.getId(), result.getId());
        assertEquals(productForm.getName(), result.getName());
        assertEquals(productForm.getDescription(), result.getDescription());
        assertEquals(productForm.getPrice(), result.getPrice());
        assertEquals(productForm.getCategoryId(), result.getCategoryId());
        assertEquals(productForm.isActive(), result.isActive());
        assertEquals(productForm.getImageFilename(), result.getImageFilename());
    }

    @Test
    void testUpdate() {
        when(productCategoryRepository.findById(anyLong())).thenReturn(Optional.of(productCategory));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.update(productForm, product);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getActive(), result.getActive());
        assertEquals(product.getFilename(), result.getFilename());
        assertEquals(product.getProductCategory(), result.getProductCategory());
    }
}


