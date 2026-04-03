package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.model.dtoRequest.InventoryDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.InventoryDtoResponse;
import com.example.StoreManagement.model.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(source = "storeID", target = "store.id")
    @Mapping(source = "productID", target = "product.id")
    Inventory dtoPostRequestToEntity(InventoryDtoPostRequest dtoPostRequest);

    @Mapping(source = "store.id", target = "storeID")
    @Mapping(source = "product.id", target = "productID")
    InventoryDtoResponse entityToDtoResponse(Inventory inventory);

    List<InventoryDtoResponse> entitiesToDtoResponse(List<Inventory> inventories);
}
