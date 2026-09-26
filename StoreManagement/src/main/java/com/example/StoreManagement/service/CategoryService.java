package com.example.StoreManagement.service;

import com.example.StoreManagement.Exception.ResourceAlreadyInUseException;
import com.example.StoreManagement.Exception.ResourceNotFoundException;
import com.example.StoreManagement.dtos.dtoRequest.CategoryPutRequest;
import com.example.StoreManagement.mapstruct.mappers.CategoryMapper;
import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PaginationUtils;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.CategoryDtoPostRequest;
import com.example.StoreManagement.dtos.dtoResponse.CategoryDtoResponse;
import com.example.StoreManagement.model.entity.Category;
import com.example.StoreManagement.repository.CategoryRepository;
import com.example.StoreManagement.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;
    private static final String entityName = "Category";

    public PagingResult<CategoryDtoResponse> findAll(PaginationRequest request) {
        Pageable pageable = PaginationUtils.getPageable(request);
        Page<Category> categoriesPage = this.categoryRepository.findByActiveTrue(pageable);

        List<CategoryDtoResponse> categoryDtoResponses = categoriesPage.stream().map(categoryMapper::entityToDtoResponse).toList();

        return new PagingResult<>(
                categoryDtoResponses,
                categoriesPage.getTotalPages(),
                categoriesPage.getTotalElements(),
                categoriesPage.getSize(),
                categoriesPage.getNumber(),
                categoriesPage.isEmpty(),
                categoriesPage.isLast()
        );
    }

    public CategoryDtoResponse findById(Long id) {
        Category category = this.categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "ID:" + id.toString()));

        return this.categoryMapper.entityToDtoResponse(category);
    }

    public List<CategoryDtoResponse> findByName(String name) {

        List<Category> category = this.categoryRepository.findByNameAndActiveTrue(name);
        return this.categoryMapper.entitiesToAllDtoResponse(category);
    }

    @Transactional
    public CategoryDtoResponse create(CategoryDtoPostRequest dtoPostRequest) {

        Category category = this.categoryRepository.findByName(dtoPostRequest.name());
        if (category != null) {
            if (category.isActive())
                throw new ResourceAlreadyInUseException(entityName, "Name:" + dtoPostRequest.name());

            category.setActive(true);
            return this.categoryMapper.entityToDtoResponse(this.categoryRepository.save(category));
        }

        category = this.categoryMapper.dtoPostToEntity(dtoPostRequest);
        return this.categoryMapper.entityToDtoResponse(this.categoryRepository.save(category));
    }

    @Transactional
    public CategoryDtoResponse update(Long id, CategoryPutRequest putRequest) {
        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "ID:" + id.toString()));

        categoryMapper.updateEntity(putRequest, category);
        categoryRepository.save(category);

        return this.categoryMapper.entityToDtoResponse(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = this.categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "ID:" + id.toString()));

        this.categoryRepository.delete(category);
    }
}

