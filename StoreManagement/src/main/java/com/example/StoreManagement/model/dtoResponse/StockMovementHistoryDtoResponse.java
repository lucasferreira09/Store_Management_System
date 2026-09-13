package com.example.StoreManagement.model.dtoResponse;
import com.example.StoreManagement.model.entity.enums.StockMovementType;
import com.example.StoreManagement.model.entity.enums.StockMovementReason;

import java.time.Instant;
import java.time.LocalDateTime;

public record StockMovementHistoryDtoResponse(
        Long id,
        StockMovementType type,
        StockMovementReason reason,
        String orderId,
        Instant createdAt,
        Long storeID,
        Long productID,
        Integer quantity,
        String description
) {}
