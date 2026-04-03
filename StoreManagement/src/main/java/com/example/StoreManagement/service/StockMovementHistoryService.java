package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.StockMovementHistoryMapper;
import com.example.StoreManagement.model.dtoResponse.StockMovementHistoryDtoResponse;
import com.example.StoreManagement.model.entity.StockMovementHistory;
import com.example.StoreManagement.model.repository.StockMovementHistoryRespository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockMovementHistoryService {
    private StockMovementHistoryRespository stockMovementHistoryRespository;
    private StockMovementHistoryMapper stockMovementHistoryMapper;

    public StockMovementHistoryService(
            StockMovementHistoryRespository stockMovementHistoryRespository,
            StockMovementHistoryMapper stockMovementHistoryMapper
    ) {
        this.stockMovementHistoryRespository = stockMovementHistoryRespository;
        this.stockMovementHistoryMapper = stockMovementHistoryMapper;
    }

    public List<StockMovementHistoryDtoResponse> findAll() {
       List<StockMovementHistory> stockMovementHistories = this.stockMovementHistoryRespository.findAll();

       return this.stockMovementHistoryMapper.entitiesToDtoResponse(stockMovementHistories);
    }
}
