package com.kacperp.mobilehub.service;

import com.kacperp.mobilehub.dto.ProductCategoryDto;
import com.kacperp.mobilehub.model.ProductCategory;

import java.util.List;

public interface IProductCategoryService {
    ProductCategoryDto findById(Long id);
    List<ProductCategoryDto> findAll();
    ProductCategoryDto convertEntityToDto(ProductCategory productCategory);
}
