package com.example.StoreManagement.model.dtoRequest;

import com.example.StoreManagement.model.entity.enums.StockMovementType;
import com.example.StoreManagement.model.entity.enums.StockMovementReason;

public record InventoryMovementRequest(
        Long inventoryID,
        StockMovementType type,
        StockMovementReason reason,
        Integer quantity,
        Integer saleId
) {}
