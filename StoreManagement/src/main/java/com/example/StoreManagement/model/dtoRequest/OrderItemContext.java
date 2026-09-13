package com.example.StoreManagement.model.dtoRequest;

import com.example.StoreManagement.model.entity.Inventory;
import com.example.StoreManagement.model.entity.Order;
import com.example.StoreManagement.model.entity.Product;
import com.example.StoreManagement.model.entity.Store;

public record OrderItemContext(
        OrderItemDtoPostRequest item,
        Inventory inventory,
        Product product,
        Store store,
        Order order
) {}
