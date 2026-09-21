package com.example.StoreManagement.repository;

import com.example.StoreManagement.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long id);
    Page<Order> findByCustomerId(Long id, Pageable pageable);

    List<Order> findByCheckoutId(UUID id);
    Page<Order> findByCheckoutId(UUID id, Pageable pageable );
}
