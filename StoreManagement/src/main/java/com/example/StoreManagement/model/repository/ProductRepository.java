package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Boolean existsByBarcode(String barcode);
    Product findByBarcode(String barcode);
    List<Product> findByName(String name);

}
