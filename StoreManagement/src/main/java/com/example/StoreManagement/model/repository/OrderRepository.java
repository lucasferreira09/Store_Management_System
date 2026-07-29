package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerId(Long id, Pageable pageable);

    List<Order> findByProviderSessionId(String id);
}
