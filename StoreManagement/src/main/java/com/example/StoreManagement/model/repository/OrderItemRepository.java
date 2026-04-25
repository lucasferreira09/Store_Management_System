package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
