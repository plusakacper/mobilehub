package com.kacperp.mobilehub.service.impl;

import com.kacperp.mobilehub.dto.ProductDto;
import com.kacperp.mobilehub.form.ProductForm;
import com.kacperp.mobilehub.model.Product;
import com.kacperp.mobilehub.model.ProductCategory;
import com.kacperp.mobilehub.repository.ProductCategoryRepository;
import com.kacperp.mobilehub.repository.ProductRepository;
import com.kacperp.mobilehub.service.IProductService;
import com.kacperp.mobilehub.util.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Value("${files.directory.path}")
    private String filesDirectoryPath;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public ProductDto findByIdAsDto(Long id)  {
        Optional<Product> product = productRepository.findById(id);

        if(product.isPresent()) {
            return convertEntityToDto(product.get());
        } else {
            return null;
        }
    }

    @Override
    public Product findByIdAsEntity(Long id) {
        Optional<Product> product = productRepository.findById(id);

        if(product.isPresent()) {
            return product.get();
        } else {
            return null;
        }
    }

    @Override
    public ProductDto findActiveByIdAsDto(Long id) {
        Optional<Product> product = productRepository.findByIdAndActiveTrue(id);

        if(product.isPresent()) {
            return convertEntityToDto(product.get());
        } else {
            return null;
        }
    }

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> convertEntityToDto(product))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<ProductDto> findAllActive() {
        List<Product> products = productRepository.findAllByActiveTrue();

        return products.stream()
                .map(product -> convertEntityToDto(product))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<ProductDto> findByProductCategoryId(Long id) {
        List<Product> products = productRepository.findByProductCategoryId(id);

        return products.stream()
                .map(product -> convertEntityToDto(product))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<ProductDto> findActiveByProductCategoryId(Long id) {
        List<Product> products = productRepository.findByProductCategoryIdAndActiveTrue(id);

        return products.stream()
                .map(product -> convertEntityToDto(product))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Product> product = productRepository.findByIdAndActiveTrue(id);
        if(product.isPresent()) {
            productRepository.delete(product.get());
        }
    }

    @Override
    @Transactional
    public Product save(ProductForm productForm) {
        Product product = convertProductFormToEntity(productForm);
        return productRepository.save(product);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public ProductDto convertEntityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice("$" + product.getPrice());
        productDto.setCategoryName(product.getProductCategory().getDisplayName());
        //String filePath = filesDirectoryPath + product.getFilename();
        productDto.setFilename(product.getFilename());
        productDto.setActive(product.getActive() == true ? "Yes" : "No");

        return productDto;
    }

    @Override
    public Product convertProductFormToEntity(ProductForm productForm) {
        Product product = new Product();
        product.setName(productForm.getName());
        product.setDescription(productForm.getDescription());
        product.setPrice(productForm.getPrice().toPlainString());
        product.setActive(productForm.isActive());
        product.setFilename(productForm.getImageFilename());
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(productForm.getCategoryId());
        if(productCategory.isPresent()) {
            product.setProductCategory(productCategory.get());
        }
        return product;
    }

    @Override
    public ProductForm convertEntityToProductForm(Product product) {
        ProductForm productForm = new ProductForm();
        productForm.setId(product.getId());
        productForm.setName(product.getName());
        productForm.setDescription(product.getDescription());
        BigDecimal price = new BigDecimal(product.getPrice());
        productForm.setPrice(price);
        productForm.setCategoryId(product.getProductCategory().getId());
        productForm.setImageFilename(product.getFilename());
        productForm.setActive(product.getActive());

        return productForm;
    }

    @Override
    public Product update(ProductForm productForm, Product product) {
        product.setName(productForm.getName());
        product.setDescription(productForm.getDescription());
        product.setPrice(productForm.getPrice().toPlainString());
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(productForm.getCategoryId());
        if(productCategory.isPresent()) {
            product.setProductCategory(productCategory.get());
        }
        product.setActive(productForm.isActive());
        if (productForm.getImageFilename() != null && !productForm.getImageFilename().isEmpty()) {
            product.setFilename(productForm.getImageFilename());
        }
        return productRepository.save(product);
    }


}
