package com.example.StoreManagement.model.dtoRequest;

import com.example.StoreManagement.model.entity.Inventory;
import com.example.StoreManagement.model.entity.enums.StockMovementType;
import com.example.StoreManagement.model.entity.enums.StockMovementReason;

import java.time.Instant;

public record StockMovementRequest(
        Inventory inventory,
        StockMovementType type,
        StockMovementReason reason,
        Integer quantity,
        String orderId,
        Instant created,
        String description
) {

    public record DtoPostRequest(
            Long inventoryId,
            StockMovementType type,
            StockMovementReason reason,
            Integer quantity,
            String orderId,
            String description
    ) {}
}
