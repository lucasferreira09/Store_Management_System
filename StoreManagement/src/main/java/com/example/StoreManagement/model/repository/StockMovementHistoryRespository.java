package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.StockMovementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMovementHistoryRespository extends JpaRepository<StockMovementHistory, Long> {

}
