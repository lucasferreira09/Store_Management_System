package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.model.dtoResponse.StockMovementHistoryDtoResponse;
import com.example.StoreManagement.model.entity.StockMovementHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMovementHistoryMapper {

    @Mapping(source = "inventory.store.id", target = "storeID")
    @Mapping(source = "inventory.product.id", target = "productID")
    StockMovementHistoryDtoResponse entityToDtoResponse(StockMovementHistory stockMovementHistory);

    List<StockMovementHistoryDtoResponse> entitiesToDtoResponse(List<StockMovementHistory> stockMovementHistories);
}
