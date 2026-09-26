package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.dtos.dtoRequest.InventoryDtoPostRequest;
import com.example.StoreManagement.dtos.dtoResponse.InventoryDtoResponse;
import com.example.StoreManagement.model.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(source = "storeId", target = "store.id")
    @Mapping(source = "productId", target = "product.id")
    Inventory dtoPostRequestToEntity(InventoryDtoPostRequest dtoPostRequest);

    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "product.id", target = "productId")
    InventoryDtoResponse entityToDtoResponse(Inventory inventory);

    List<InventoryDtoResponse> entitiesToDtoResponse(List<Inventory> inventories);
}
