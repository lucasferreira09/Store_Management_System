package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.StockMovementHistoryMapper;
import com.example.StoreManagement.model.dtoResponse.StockMovementHistoryDtoResponse;
import com.example.StoreManagement.model.entity.StockMovementHistory;
import com.example.StoreManagement.model.repository.StockMovementHistoryRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockMovementHistoryService {
    private final StockMovementHistoryRespository stockMovementHistoryRespository;
    private final StockMovementHistoryMapper stockMovementHistoryMapper;


    public List<StockMovementHistoryDtoResponse> findAll() {
       List<StockMovementHistory> stockMovementHistories = this.stockMovementHistoryRespository.findAll();

       return this.stockMovementHistoryMapper.entitiesToDtoResponse(stockMovementHistories);
    }
}
