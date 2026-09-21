package com.example.StoreManagement.repository;

import com.example.StoreManagement.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrueAndCategoryActiveTrue();
    Page<Product> findByActiveTrueAndCategoryActiveTrue(Pageable pageable);

    Optional<Product> findByIdAndActiveTrueAndCategoryActiveTrue(Long id);
    List<Product> findByNameAndActiveTrueAndCategoryActiveTrue(String name);
    Product findByBarcode(String barcode);
    Product findByBarcodeAndActiveTrue(String barcode);
    List<Product> findByCategoryIdAndActiveTrue(Long id);

}
