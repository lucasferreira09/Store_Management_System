package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.model.dtoRequest.CategoryDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CategoryDtoResponse;
import com.example.StoreManagement.model.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category dtoPostToEntity(CategoryDtoPostRequest categoryDtoPostRequest);
    CategoryDtoResponse entityToDtoResponse(Category category);

    List<CategoryDtoResponse> entitiesToAllDtoResponse(List<Category> categories);

}
