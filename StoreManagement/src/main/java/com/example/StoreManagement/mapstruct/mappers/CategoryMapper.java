package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.dtos.dtoRequest.CategoryDtoPostRequest;
import com.example.StoreManagement.dtos.dtoRequest.CategoryPutRequest;
import com.example.StoreManagement.dtos.dtoResponse.CategoryDtoResponse;
import com.example.StoreManagement.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category dtoPostToEntity(CategoryDtoPostRequest categoryDtoPostRequest);
    CategoryDtoResponse entityToDtoResponse(Category category);

    List<CategoryDtoResponse> entitiesToAllDtoResponse(List<Category> categories);

    void updateEntity(CategoryPutRequest putRequest, @MappingTarget Category category);
}
