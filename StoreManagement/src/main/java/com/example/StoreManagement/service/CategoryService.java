package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.CategoryMapper;
import com.example.StoreManagement.model.dtoRequest.CategoryDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CategoryDtoResponse;
import com.example.StoreManagement.model.entity.Category;
import com.example.StoreManagement.model.entity.Product;
import com.example.StoreManagement.model.repository.CategoryRepository;
import com.example.StoreManagement.model.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;

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

    public List<CategoryDtoResponse> findAll() {
        List<Category> allCategories = this.categoryRepository.findAll();

        return this.categoryMapper.entitiesToAllDtoResponse(allCategories);
    }

    public CategoryDtoResponse findById(Long id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found!"));

        return this.categoryMapper.entityToDtoResponse(category);
    }

    public List<CategoryDtoResponse> findByName(String name) {

        List<Category> category = this.categoryRepository.findByName(name);
        return this.categoryMapper.entitiesToAllDtoResponse(category);
    }

    public CategoryDtoResponse findByProductId(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("This product doesn't exists"));

        Category category = product.getCategory();

        return this.categoryMapper.entityToDtoResponse(category);
    }

    public CategoryDtoResponse create(CategoryDtoPostRequest categoryDtoPostRequest) {
        Category categoryEntity = this.categoryMapper.dtoPostToEntity(categoryDtoPostRequest);

        return this.categoryMapper.entityToDtoResponse(this.categoryRepository.save(categoryEntity));
    }

    public CategoryDtoResponse update(Long id, CategoryDtoPostRequest categoryDtoPostRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with this id"));

        Category updatedCategory = this.categoryMapper.dtoPostToEntity(categoryDtoPostRequest);
        updatedCategory.setId(category.getId());

        this.categoryRepository.save(updatedCategory);

        return this.categoryMapper.entityToDtoResponse(updatedCategory);
    }

    public void delete(Long id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with this id"));

        this.categoryRepository.delete(category);
    }
}
