package com.kacperp.mobilehub.service;

import com.kacperp.mobilehub.dto.ProductDto;
import com.kacperp.mobilehub.form.OrderForm;
import com.kacperp.mobilehub.form.ProductForm;
import com.kacperp.mobilehub.model.Order;
import com.kacperp.mobilehub.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    ProductDto findByIdAsDto(Long id);
    Product findByIdAsEntity(Long id);
    ProductDto findActiveByIdAsDto(Long id);
    List<ProductDto> findAll();
    List<ProductDto> findAllActive();
    List<ProductDto> findByProductCategoryId(Long id);
    List<ProductDto> findActiveByProductCategoryId(Long id);
    void delete(Long id);
    Product save(ProductForm productForm);
    Product save(Product product);
    ProductDto convertEntityToDto(Product product);
    Product convertProductFormToEntity(ProductForm productForm);
    ProductForm convertEntityToProductForm(Product product);
    Product update(ProductForm productForm, Product product);
}
