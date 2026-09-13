package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.CategoryMapper;
import com.example.StoreManagement.model.dtoRequest.CategoryDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CategoryDtoResponse;
import com.example.StoreManagement.model.entity.Category;
import com.example.StoreManagement.model.repository.CategoryRepository;
import com.example.StoreManagement.model.repository.ProductRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;


    public List<CategoryDtoResponse> findAll() {
        List<Category> categories = this.categoryRepository.findByActiveTrue();

        return this.categoryMapper.entitiesToAllDtoResponse(categories);
    }

    public CategoryDtoResponse findById(Long id) {
        Category category = this.categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found!"));

        return this.categoryMapper.entityToDtoResponse(category);
    }

    public List<CategoryDtoResponse> findByName(String name) {

        List<Category> category = this.categoryRepository.findByNameAndActiveTrue(name);
        return this.categoryMapper.entitiesToAllDtoResponse(category);
    }

    public CategoryDtoResponse create(CategoryDtoPostRequest dtoPostRequest) {

        Category category = this.categoryRepository.findByName(dtoPostRequest.name());
        if (category != null) {
            if (category.isActive())
                throw new EntityExistsException("Category already exists");

            category.setActive(true);
            return this.categoryMapper.entityToDtoResponse(this.categoryRepository.save(category));
        }

        category = this.categoryMapper.dtoPostToEntity(dtoPostRequest);
        return this.categoryMapper.entityToDtoResponse(this.categoryRepository.save(category));
    }


    public CategoryDtoResponse update(Long id, CategoryDtoPostRequest dtoPostRequest) {
        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with this ID"));

        Category existingByName = this.categoryRepository.findByName(dtoPostRequest.name());
        if (existingByName != null)
            throw new EntityExistsException("Category already in use");

        Category updatedCategory = this.categoryMapper.dtoPostToEntity(dtoPostRequest);
        updatedCategory.setId(category.getId());

        this.categoryRepository.save(updatedCategory);
        return this.categoryMapper.entityToDtoResponse(updatedCategory);
    }

    public void delete(Long id) {
        Category category = this.categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with this id"));

        this.categoryRepository.delete(category);
    }
}

