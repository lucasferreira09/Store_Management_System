package com.example.StoreManagement.model.dtoResponse;
import com.example.StoreManagement.model.entity.enums.StockMovementType;
import com.example.StoreManagement.model.entity.enums.StockMovementReason;

import java.time.LocalDateTime;

public record StockMovementHistoryDtoResponse(
        StockMovementType type,
        StockMovementReason reason,
        String saleId,
        LocalDateTime createdAt,
        Long storeID,
        Long productID,
        Integer quantity
) {}
