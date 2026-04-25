package com.example.StoreManagement.model.dtoResponse;

import java.math.BigDecimal;

public record OrderItemDtoResponse(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal salePrice,
        BigDecimal totalPrice
) {}
