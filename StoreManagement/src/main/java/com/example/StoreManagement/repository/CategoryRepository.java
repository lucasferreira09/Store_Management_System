package com.example.StoreManagement.repository;

import com.example.StoreManagement.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByActiveTrue();
    Page<Category> findByActiveTrue(Pageable pageable);
    List<Category> findByNameAndActiveTrue(String name);
    Category findByName(String name);
    Optional<Category> findByIdAndActiveTrue(Long id);
}
