package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByName(String name);

}
