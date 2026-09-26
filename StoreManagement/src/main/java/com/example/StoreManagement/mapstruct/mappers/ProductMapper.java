package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.dtos.dtoRequest.ProductDtoPostRequest;
import com.example.StoreManagement.dtos.dtoRequest.ProductDtoPutRequest;
import com.example.StoreManagement.dtos.dtoResponse.ProductDtoResponse;
import com.example.StoreManagement.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

    Product dtoPostRequestToEntity(ProductDtoPostRequest productDtoPostRequest);
    Product dtoPutRequestToEntity(ProductDtoPutRequest productDtoPutRequest);

    @Mapping(source = "category.id", target = "categoryId")
    ProductDtoResponse entityToDtoResponse(Product product);

    List<ProductDtoResponse> entitiesToAllDtoResponse(List<Product> products);

    void updateEntity(ProductDtoPutRequest putRequest, @MappingTarget Product product);

}
