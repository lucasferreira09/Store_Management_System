package com.example.StoreManagement.dtos.dtoResponse;

import java.math.BigDecimal;

public record OrderItemDtoResponse(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal salePrice
) {}
