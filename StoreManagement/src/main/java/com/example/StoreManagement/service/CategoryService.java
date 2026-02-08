package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.CategoryMapper;
import com.example.StoreManagement.model.dtoRequest.CategoryDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CategoryDtoResponse;
import com.example.StoreManagement.model.dtoResponse.ProductDtoResponse;
import com.example.StoreManagement.model.entity.Category;
import com.example.StoreManagement.model.entity.Product;
import com.example.StoreManagement.model.repository.CategoryRepository;
import com.example.StoreManagement.model.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productRepository = productRepository;
    }

    public List<CategoryDtoResponse> getAllCategories() {
        List<Category> allCategories = this.categoryRepository.findAll();

        return this.categoryMapper.entitiesToAllDtoResponse(allCategories);
    }

    public CategoryDtoResponse getCategoryById(Long id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found!"));

        return this.categoryMapper.entityToDtoResponse(category);
    }

    public List<CategoryDtoResponse> getCategoryByName(String name) {

        List<Category> category = this.categoryRepository.findByName(name);
        return this.categoryMapper.entitiesToAllDtoResponse(category);
    }

    public CategoryDtoResponse getCategoryByProductID(Long productID) {
        Product product = this.productRepository.findById(productID).orElseThrow(
                () -> new EntityNotFoundException("This product doesn't exists"));

        Category category = product.getCategory();

        return this.categoryMapper.entityToDtoResponse(category);
    }


    public CategoryDtoResponse addCategory(CategoryDtoPostRequest categoryDtoPostRequest) {
        Category categoryEntity = this.categoryMapper.dtoPostToEntity(categoryDtoPostRequest);

        return this.categoryMapper.entityToDtoResponse(this.categoryRepository.save(categoryEntity));
    }
}
