package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.model.dtoRequest.ProductDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.ProductDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.ProductDtoResponse;
import com.example.StoreManagement.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

    Product dtoPostRequestToEntity(ProductDtoPostRequest productDtoPostRequest);
    Product dtoPutRequestToEntity(ProductDtoPutRequest productDtoPutRequest);

    @Mapping(source = "category.id", target = "categoryID")
    ProductDtoResponse entityToDtoResponse(Product product);

    List<ProductDtoResponse> entitiesToAllDtoResponse(List<Product> products);

}
