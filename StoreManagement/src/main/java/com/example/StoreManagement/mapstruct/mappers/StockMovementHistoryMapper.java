package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.dtos.dtoResponse.StockMovementHistoryDtoResponse;
import com.example.StoreManagement.model.entity.StockMovementHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMovementHistoryMapper {

    @Mapping(source = "inventory.store.id", target = "storeId")
    @Mapping(source = "inventory.product.id", target = "productId")
    StockMovementHistoryDtoResponse entityToDtoResponse(StockMovementHistory stockMovementHistory);

    List<StockMovementHistoryDtoResponse> entitiesToDtoResponse(List<StockMovementHistory> stockMovementHistories);
}
