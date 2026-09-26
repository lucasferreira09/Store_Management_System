package com.example.StoreManagement.dtos.dtoResponse;
import com.example.StoreManagement.enums.StockMovementType;
import com.example.StoreManagement.enums.StockMovementReason;

import java.time.Instant;

public record StockMovementHistoryDtoResponse(
        Long id,
        StockMovementType type,
        StockMovementReason reason,
        String orderId,
        Instant createdAt,
        Long storeId,
        Long productId,
        Integer quantity,
        String description
) {}
