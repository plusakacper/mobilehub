package com.kacperp.mobilehub.repository;

import com.kacperp.mobilehub.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductCategoryId(Long id);
    List<Product> findByProductCategoryIdAndActiveTrue(Long id);
    Optional<Product> findByIdAndActiveTrue(Long id);
    List<Product> findAllByActiveTrue();
}
