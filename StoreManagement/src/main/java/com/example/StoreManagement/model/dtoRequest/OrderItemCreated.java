package com.example.StoreManagement.model.dtoRequest;

import com.example.StoreManagement.model.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemCreated(
        OrderItem orderItem,
        BigDecimal totalPrice
) {}
