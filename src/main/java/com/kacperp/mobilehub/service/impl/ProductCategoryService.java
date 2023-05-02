package com.kacperp.mobilehub.service.impl;

import com.kacperp.mobilehub.dto.ProductCategoryDto;
import com.kacperp.mobilehub.model.ProductCategory;
import com.kacperp.mobilehub.repository.ProductCategoryRepository;
import com.kacperp.mobilehub.service.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService implements IProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public ProductCategoryDto findById(Long id) {
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(id);
        if(productCategory.isPresent()) {
            return convertEntityToDto(productCategory.get());
        } else {
            return null;
        }
    }

    @Override
    public List<ProductCategoryDto> findAll() {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        return productCategories.stream()
                .map(productCategory -> convertEntityToDto(productCategory))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ProductCategoryDto convertEntityToDto(ProductCategory productCategory) {
        ProductCategoryDto productCategoryDto = new ProductCategoryDto();
        productCategoryDto.setId(productCategory.getId());
        productCategoryDto.setName(productCategory.getName());
        productCategoryDto.setDisplayName(productCategory.getDisplayName());
        return productCategoryDto;
    }

}
