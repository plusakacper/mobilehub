package com.kacperp.mobilehub.unit;

import com.kacperp.mobilehub.dto.ProductCategoryDto;
import com.kacperp.mobilehub.model.ProductCategory;
import com.kacperp.mobilehub.repository.ProductCategoryRepository;
import com.kacperp.mobilehub.service.impl.ProductCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ProductCategoryServiceTest {

    private ProductCategoryRepository productCategoryRepository = mock(ProductCategoryRepository.class);

    private ProductCategoryService productCategoryService = new ProductCategoryService(productCategoryRepository);

    private ProductCategory category1;
    private ProductCategory category2;

    @BeforeEach
    void setUp() {

        category1 = new ProductCategory();
        category1.setId(1L);
        category1.setName("category1");
        category1.setDisplayName("Category 1");

        category2 = new ProductCategory();
        category2.setId(2L);
        category2.setName("category2");
        category2.setDisplayName("Category 2");
    }

    @Test
    void testFindById() {
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(productCategoryRepository.findById(2L)).thenReturn(Optional.of(category2));

        ProductCategoryDto result1 = productCategoryService.findById(1L);
        ProductCategoryDto result2 = productCategoryService.findById(2L);

        assertNotNull(result1);
        assertEquals(category1.getId(), result1.getId());
        assertEquals(category1.getName(), result1.getName());
        assertEquals(category1.getDisplayName(), result1.getDisplayName());

        assertNotNull(result2);
        assertEquals(category2.getId(), result2.getId());
        assertEquals(category2.getName(), result2.getName());
        assertEquals(category2.getDisplayName(), result2.getDisplayName());
    }

    @Test
    void testFindAll() {
        when(productCategoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<ProductCategoryDto> result = productCategoryService.findAll();

        assertEquals(2, result.size());

        ProductCategoryDto result1 = result.get(0);
        assertEquals(category1.getId(), result1.getId());
        assertEquals(category1.getName(), result1.getName());
        assertEquals(category1.getDisplayName(), result1.getDisplayName());

        ProductCategoryDto result2 = result.get(1);
        assertEquals(category2.getId(), result2.getId());
        assertEquals(category2.getName(), result2.getName());
        assertEquals(category2.getDisplayName(), result2.getDisplayName());
    }

    @Test
    void testConvertEntityToDto() {
        ProductCategoryDto result = productCategoryService.convertEntityToDto(category1);

        assertNotNull(result);
        assertEquals(category1.getId(), result.getId());
        assertEquals(category1.getName(), result.getName());
        assertEquals(category1.getDisplayName(), result.getDisplayName());
    }
}